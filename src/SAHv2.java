import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.io.*;
import java.util.*;

/**
 * Class which solves the Savings Heuristic Vehicle Routing Problem. We have:
 *  a set of SmartPoints, each one with its capacity constraints,
 *  a set of parcels, each one with its delivery location, a penalty for not delivering it and its size
 *  a fleet of unlimited identical vehicles, with its capacity constraints
 *  constants variables for the vehicle cost, running cost, loading cost and duration of a shift
 *  a time matrix containing the travel duration between the SmartPoints in minutes
 * *
 * By solving an LP program we get the max number of parcels we can deliver and their delivery locations ->
 * we have at most smartPoints number of routes ( 0->1-> 0 , 0->2->0 e.t.c.), we implement the savings heuristic for
 * route merging.
 * *
 * Each parcel has up to three possible delivery location, and the sizes of the cells are nested
 * i.e. a Small parcel can fit on a Medium or Large cell of a SmartPoint.
 */
public class SAHv2 {

    private final String MIPProperties;
    private final String SPFilename;
    private final String parcelFilename;
    private final String outputFilename;
    private int smartPoints;
    private int[][] locationCapacity;
    private int[] vehicleCapacity;
    private double[][] travelMatrix;
    private int parcels;
    private long[] parcelID;
    private int[] parcelSize;
    private int[][] parcelLocation;
    private int timeShift;
    private double vehicleCost;
    private double runningCost;
    private double loadingCost;
    private static final int sizes = 3;
    private static final int deliveryLocations = 3;

    /**
     * Constructor of this class.
     * @param SPFilename ,contains the input data considering the Vehicle and SmartPoint parameters.
     * @param parcelFilename ,contains the input data considering the Parcel parameters.
     * @param outputFilename ,file for writing the output.
     * @param properties ,contains tuning for the LP solver.
     */
    public SAHv2(String SPFilename, String parcelFilename, String outputFilename, String properties) {
        this.SPFilename = SPFilename;
        this.parcelFilename = parcelFilename;
        this.outputFilename = outputFilename;
        this.MIPProperties = properties;
    }

    /**
     * Method used to execute the solver.
     */
    private void execute(){
        loadData();
        solve();
    }

    /**
     * Method used to load the input data.
     */
    private void loadData(){
        // initialize the Smart Point parameters from the input file //
        try(BufferedReader reader=new BufferedReader(new FileReader(SPFilename))){

            String[] inputs = reader.readLine().split(" ",4);

            vehicleCost = Double.parseDouble(inputs[0]);
            runningCost = Double.parseDouble(inputs[1]);
            loadingCost = Double.parseDouble(inputs[2]);
            timeShift = Integer.parseInt(inputs[3]);

            reader.readLine();

            inputs = reader.readLine().split(" ",sizes);

            vehicleCapacity = new int[sizes];
            for (int i = 0; i < sizes; i++)
                vehicleCapacity[i] = Integer.parseInt(inputs[i]);

            reader.readLine();

            smartPoints = Integer.parseInt(reader.readLine());

            reader.readLine();

            locationCapacity = new int[smartPoints][sizes];
            for (int i = 0; i < smartPoints; i++){

                inputs = reader.readLine().split(" ",sizes);

                for (int j = 0; j < sizes; j++)
                    locationCapacity[i][j] = Integer.parseInt(inputs[j]);
            }

            reader.readLine();

            travelMatrix = new double[smartPoints][smartPoints];
            for (int i = 0; i < smartPoints; i++){

                inputs = reader.readLine().split(" ",smartPoints);

                for (int j = 0; j < smartPoints; j++)
                    travelMatrix[i][j] = Double.parseDouble(inputs[j]);
            }
        } catch (IOException e) {
            System.err.println("Error: Could not open the input File! " + e.getMessage());
        }

        // initialize the Parcel parameters from the input file //
        try(BufferedReader reader=new BufferedReader(new FileReader(parcelFilename))){

            String input = reader.readLine();
            parcels = Integer.parseInt(input);

            reader.readLine();

            parcelID = new long[parcels];
            parcelSize = new int[parcels];
            parcelLocation = new int[parcels][deliveryLocations];

            for (int i = 0; i < parcels; i++){

                String[] inputs = reader.readLine().split(" ", deliveryLocations + 3);

                parcelID[i] = Long.parseLong(inputs[0]);
                parcelSize[i] = Integer.parseInt(inputs[1]);

                for (int j = 0; j < deliveryLocations; j++)
                    parcelLocation[i][j] = Integer.parseInt(inputs[j + 3]);
            }
        } catch (IOException e) {
            System.err.println("Error: Could not open the input File! " + e.getMessage());
        }
    }

    /**
     * Method used to solve the LP problem referring to the delivery location of the parcels.
     * It maximizes the number of delivered parcels and applies the savings heuristic method to merge routes.
     */
    private void solve() {

        final long startTime = System.currentTimeMillis();

        /////////////////////////////////////// Helper Structures //////////////////////////////////////////////

        // contains max number of parcels size j each SmartPoint can store //
        int[][] maxLocationCapacity = new int[smartPoints][sizes];

        for (int i = 0; i < smartPoints; i++) {
            for (int j = 0; j < sizes; j++) {
                maxLocationCapacity[i][j] = locationCapacity[i][j];
                for (int k = j + 1; k < sizes; k++)
                    maxLocationCapacity[i][j] += locationCapacity[i][k];
            }
        }

        // contains a list of all the parcels size j //
        ArrayList<ArrayList<Integer>> parcelsPerSize = new ArrayList<>(new ArrayList<>());

        // initialization //
        for (int i = 0; i < sizes; i++)
            parcelsPerSize.add(new ArrayList<>());

        for (int q = 0; q < parcels; q++)
            parcelsPerSize.get(parcelSize[q]).add(q);

        // contains a list of all the possible SmartPoints each parcel can be delivered to//
        ArrayList<ArrayList<Integer>> locationsPerParcel = new ArrayList<>(new ArrayList<>());

        for (int q = 0; q < parcels; q++) {
            //initialization//
            locationsPerParcel.add(new ArrayList<>());

            for (int l = 0; l < deliveryLocations; l++) {
                if (parcelLocation[q][l] != 0)
                    locationsPerParcel.get(q).add(parcelLocation[q][l]);
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        try(IloCplex cplex = new IloCplex()){

            ////////////////////////////////////Solver Parameters //////////////////////////////////////////////

            Properties mipProperties = new Properties();
            mipProperties.load(new FileInputStream(MIPProperties));

            cplex.setParam(
                    IloCplex.Param.Threads,
                    Integer.parseInt(mipProperties.getProperty("threads"))
            );

            cplex.setParam(
                    IloCplex.Param.TimeLimit,
                    Double.parseDouble(mipProperties.getProperty("timeLimit"))
            );

            cplex.setParam(
                    IloCplex.Param.MIP.Tolerances.MIPGap,
                    Double.parseDouble(mipProperties.getProperty("mipGAP"))
            );

            ////////////////////////////////////////////////////////////////////////////////////////////////////

            //////////////////////////////////// Setting Up Main Objective /////////////////////////////////////

            IloNumVar[][] delivery = new IloNumVar[parcels][smartPoints];

            for (int q = 0; q < parcels; q++)
                delivery[q] = cplex.boolVarArray(smartPoints);

            // sums up all the delivered parcels //
            IloNumExpr deliveryObjective = cplex.linearNumExpr();

            for (int q = 0; q < parcels; q++) {

                IloLinearNumExpr possibleLocations = cplex.linearNumExpr();
                //check its one of the three possible locations//
                for (int l = 0; l < deliveryLocations; l++) {
                    if (parcelLocation[q][l] != 0)
                        possibleLocations.addTerm(1.0, delivery[q][parcelLocation[q][l]]);
                }

                deliveryObjective = cplex.sum(deliveryObjective,possibleLocations);
            }

            // main objective = max { deliveryObjective } //
            cplex.addMaximize(deliveryObjective);

            ////////////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////// Setting up all the Constraints ///////////////////////////////////////

            System.out.println("\n" + cplex.getObjective());

            System.out.println("\nConstraint 1: Capacity Constraint for each SP.");

            for (int i = 1; i < smartPoints; i++) {

                for (int j = 0; j < sizes; j++) {

                    IloLinearNumExpr delivered = cplex.linearNumExpr();
                    IloLinearNumExpr deliveredBigger = cplex.linearNumExpr();

                    for (int q = 0; q < parcelsPerSize.get(j).size(); q++) {
                        if (locationsPerParcel.get(parcelsPerSize.get(j).get(q)).contains(i))
                            delivered.addTerm(1.0, delivery[parcelsPerSize.get(j).get(q)][i]);
                    }

                    for (int k = j + 1; k < sizes; k++) {
                        for (int q = 0; q < parcelsPerSize.get(k).size(); q++) {
                            if (locationsPerParcel.get(parcelsPerSize.get(k).get(q)).contains(i))
                                deliveredBigger.addTerm(1.0, delivery[parcelsPerSize.get(k).get(q)][i]);
                        }
                    }

                    cplex.addLe(delivered, cplex.sum(maxLocationCapacity[i][j], cplex.prod(-1.00, deliveredBigger)));
                }
            }

            System.out.println("\nConstraint 2: Choosing if we deliver each parcel.\n");

            for (int q = 0; q < parcels; q++) {

                IloLinearNumExpr delivered = cplex.linearNumExpr();

                for (int l = 0; l < locationsPerParcel.get(q).size(); l++)
                    delivered.addTerm(1.0, delivery[q][locationsPerParcel.get(q).get(l)]);

                cplex.addLe(delivered, 1.0);
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////

            /////////////////////////////////// Solving the problem ////////////////////////////////////////////

            if (cplex.solve()) {

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {

                    writer.write("Total execution time: " +
                            Math.round(((System.currentTimeMillis() - startTime) / 60000.0) * 10000) / 10000.0 +
                            " minutes.\n");

                    writer.write("\nParcels delivered: " + (int)cplex.getObjValue()+ " / " + parcels + "\n");

                    writer.write("\n---Parcel Delivery Location---\n");

                    for (int q = 0; q < parcels; q++) {
                        for (int j = 1; j < smartPoints; j++) {
                            if (locationsPerParcel.get(q).contains(j) && cplex.getValue(delivery[q][j]) == 1.00)
                                writer.write("ID: " + parcelID[q]  + " -> " + j + "\n");
                        }
                    }
                    writer.write("------------------------------\n");

                } catch (IOException e) {
                    System.out.println("Error: couldn't write the output on a file!");
                    throw new RuntimeException(e);
                }

                // contains the list of parcels to be delivered to each SmartPoint //
                HashMap<Integer,ArrayList<Long>> parcelsPerLocation = new HashMap<>();
                // contains the number of parcels of each size for every SmartPoint //
                HashMap<Integer,int[]> sizesPerLocation = new HashMap<>();

                // initialization //
                for (int i = 1; i < smartPoints; i++){
                    parcelsPerLocation.put(i,new ArrayList<>());
                    sizesPerLocation.put(i, new int[]{0,0,0});
                }

                for (int q = 0; q < parcels; q++) {
                    for (int j = 1; j < smartPoints; j++) {
                        if (locationsPerParcel.get(q).contains(j) && cplex.getValue(delivery[q][j]) == 1.00) {
                            parcelsPerLocation.get(j).add(parcelID[q]);
                            sizesPerLocation.get(j)[parcelSize[q]]++;
                        }
                    }
                }

                // apply savings heuristic //
                savingsApproach(parcelsPerLocation,sizesPerLocation);

                ////////////////////////////////////////////////////////////////////////////////////////////////////
            } else {
                System.out.println("No solution found.");
            }

        } catch (IloException | IOException e) {
            System.err.println("Concert exception caught: " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Method implementing the savings heuristic.
     * @param parcelsPerLocation ,map containing the parcels to be delivered to each smartPoint.
     * @param sizesPerLocation ,map containing the number of parcels of each size for each smartPoint.
     */
    private void savingsApproach(HashMap<Integer, ArrayList<Long>> parcelsPerLocation, HashMap<Integer,int[]> sizesPerLocation) {

        // used in order to store all the routes //
        HashMap<Integer,List<Integer>> routes = new HashMap<>();

        int depot = 0;

        // we initialise the routes by adding a route for each smartPoint//
        // 0 -> smartPoint -> 0 , time[i] = distance to depot            //
        for (int i = 1; i < smartPoints; i++) {

            // we will only add a route if we have parcels to deliver to this smartPoint //
            if (!parcelsPerLocation.get(i).isEmpty())
                routes.put(i, new ArrayList<>(Arrays.asList(depot, i, depot)));
        }

        // keep a list for each saving between (i,j) smartPoint pairs //
        ArrayList<Saving> savingsList = new ArrayList<>();

        // compute all the savings between (i,j) pairs and add them to savingsList//
        for (int i = 1; i < smartPoints; i++){
            for (int j = i + 1; j < smartPoints; j++) {

                // making sure we add savings(i,j) only if we deliver to smartPoint i and smartPoint j //
                if (routes.containsKey(i) & routes.containsKey(j)) {
                    double save = travelMatrix[depot][i] + travelMatrix[depot][j] - travelMatrix[i][j] + vehicleCost;
                    // if saving < 0, there is no reason to merge the pair in one route //
                    if (save > 0)
                        savingsList.add(new Saving(i, j, save));
                }
            }
        }

        // sort the savingsList in DESC ORDER//
        savingsList.sort((a,b) -> Double.compare(b.saving, a.saving));

        for (Saving saving : savingsList) {

            int start = saving.i;
            int end = saving.j;

            List<Integer> routeStart = findRoute(routes,start);
            List<Integer> routeEnd = findRoute(routes,end);

            // we have to make sure the two smartPoints don't already belong to the same route //
            // we have to check the capacity and time constraints //
            if (routeStart != routeEnd && canMerge(start,end,Objects.requireNonNull(routeStart),Objects.requireNonNull(routeEnd),sizesPerLocation,parcelsPerLocation))
                mergeRoutes(routes, routeStart, routeEnd, start, end);
        }

        writeArrivingTimes(routes,parcelsPerLocation);

        writeRoutes(routes);

        writeTransportationCost(routes);
    }

    /**
     * Method which returns the route containing the source SmartPoint.
     * @param routes ,map containing all the routes.
     * @param source ,the smartPoint in question.
     * @return List<Integer> which refers to the route containing the source smartPoint.
     */
    private List<Integer> findRoute(HashMap<Integer, List<Integer>> routes, int source) {

        for (List<Integer> route : routes.values()) {
            if (route.contains(source))
                return route;
        }
        return null;
    }

    /**
     * Method which checks the capacity and time constraints of uniting two smartPoints and their routes.
     * @param start ,refers to the one half of the pair in question of merging.
     * @param end ,refers to the second half of the pair in question of merging.
     * @param routeStart ,refers to the route containing the first smartPoint.
     * @param routeEnd ,refers to the route containing the second smartPoint.
     * @param sizesPerLocation ,used for checking the capacity constraints of the vehicle.
     * @param parcelsPerLocation ,used for checking the time constraints of arriving at each SmartPoint.
     * @return true if the merging of the two routes is possible, false otherwise.
     */
    private boolean canMerge(int start, int end, List<Integer> routeStart, List<Integer> routeEnd, HashMap<Integer, int[]> sizesPerLocation, HashMap<Integer, ArrayList<Long>> parcelsPerLocation) {

        // we need to check if the pair is interior //
        for (int i = 0; i < routeStart.size(); i++){
            if (routeStart.get(i) == start &&  (i != 1 || i != routeStart.size() - 2))
                return false;
        }

        for (int i = 0; i < routeEnd.size(); i++){
            if (routeEnd.get(i) == end &&  (i != 1 || i != routeStart.size() - 2))
                return false;
        }

        // we need to check the capacity constraints of the vehicle //
        int smallParcels = 0;
        int mediumParcels= 0;
        int largeParcels = 0;

        // we add all the parcels of each size for the two routes in consideration of merging //
        for (int point : routeStart){
            if (sizesPerLocation.containsKey(point)){
                smallParcels += sizesPerLocation.get(point)[0];
                mediumParcels += sizesPerLocation.get(point)[1];
                largeParcels += sizesPerLocation.get(point)[2];
            }
        }

        for (int point : routeEnd){
            if (sizesPerLocation.containsKey(point)){
                smallParcels += sizesPerLocation.get(point)[0];
                mediumParcels += sizesPerLocation.get(point)[1];
                largeParcels += sizesPerLocation.get(point)[2];
            }
        }

        // remaining cells where one parcel of smaller size can fit //
        int remainingLarge = Math.max(vehicleCapacity[2] - largeParcels, 0);
        int remainingMedium = Math.max(vehicleCapacity[1] + remainingLarge - mediumParcels,0);

        // if the parcels of the two routes don't fit on one vehicle -> we cannot merge the two routes //
        if (!(largeParcels <= vehicleCapacity[2] &&
                mediumParcels <= vehicleCapacity[1] + remainingLarge &&
                smallParcels <= vehicleCapacity[0] + remainingMedium))
            return false;


        // we need to check the time constraints for arriving time to each smartPoint of the merged route //

        // we merge the two routes  //
        // the merging will either be  0 -> route1 -> route2 -> 0 OR 0 -> route2 -> route1 -> 0 //
        List<Integer> mergedRoute = new ArrayList<>();

        if (routeStart.get(routeStart.size() - 2) == start && routeEnd.get(1) == end) {
            mergedRoute = new ArrayList<>(routeStart);
            //remove depot //
            mergedRoute.remove(routeStart.size() - 1);
            // merge the second route onto to the first //
            mergedRoute.addAll(routeEnd.subList(1, routeEnd.size()));
        } else if (routeEnd.get(routeEnd.size() - 2) == end && routeStart.get(1) == start) {
            mergedRoute = new ArrayList<>(routeEnd);
            //remove depot //
            mergedRoute.remove(routeEnd.size() - 1);
            // merge the first route onto the second //
            mergedRoute.addAll(routeStart.subList(1, routeStart.size()));
        }

        // after creating the merged route we can check for time constraint for each smartPoint //
        double[] time = new double[mergedRoute.size()];

        // arriving time at depot = 0 //
        time[0] = 0;
        // arriving time at 1st smartPoint of the Route = travelMatrix[0][smartPoint] //
        time[1] = travelMatrix[0][mergedRoute.get(1)];

        for (int i = 2; i < mergedRoute.size(); i++)
            // arriving time at smartPoint (i) = arriving time oa SmartPoint (i-1) + time to unload parcels at (i-1) + travelMatrix[i-1][i] //
            time[i] = time[i - 1] + loadingCost * parcelsPerLocation.get(mergedRoute.get(i-1)).size() + travelMatrix[mergedRoute.get(i - 1)][mergedRoute.get(i)] ;

        // if time[i] + unloading parcels at (i) + travelDistance[i][0] > TimeShift , we can't merge the routes //
        for (int i = 1; i < mergedRoute.size() - 1; i++){
            if (time[i] + loadingCost * parcelsPerLocation.get(mergedRoute.get(i)).size() + travelMatrix[mergedRoute.get(i)][0] > timeShift)
                return false;
        }

        return true;
    }

    /**
     * Method that merges two routes, one containing smartPoint1 and the second containing smartPoint2.
     * @param routes ,map containing all the routes.
     * @param route1 ,route containing smartPoint1.
     * @param route2 ,route containing smartPoint2.
     * @param smartPoint1 ,first smartPoint in question of merging.
     * @param smartPoint2 ,second smartPoint in question of merging.
     */
    private void mergeRoutes(Map<Integer, List<Integer>> routes, List<Integer> route1, List<Integer> route2, int smartPoint1, int smartPoint2) {

        // remove redundant depot from one of the routes //
        if (route1.get(route1.size() - 2) == smartPoint1 && route2.get(1) == smartPoint2) {
            //remove depot //
            route1.remove(route1.size() - 1);
            // merge the second route onto to the first //
            route1.addAll(route2.subList(1, route2.size()));
            // remove the second route from the map//
            routes.values().remove(route2);
        } else if (route2.get(route2.size() - 2) == smartPoint2 && route1.get(1) == smartPoint1) {
            //remove depot //
            route2.remove(route2.size() - 1);
            // merge the first route onto the second//
            route2.addAll(route1.subList(1, route1.size()));
            // remove the first route from the map//
            routes.values().remove(route1);
        }
    }

    /**
     * Method used to write the routes on an output file.
     * @param routes ,map containing all the routes.
     */
    private void writeRoutes(HashMap<Integer, List<Integer>> routes) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename,true))) {

            int counter = 1;

            writer.write("\n------------Routes------------\n");

            for (List<Integer> route : routes.values()){

                writer.write("Route " + counter++ + ": ");

                for (int i = 0; i < route.size() - 1; i++)
                    writer.write(route.get(i) + "->");

                writer.write(route.get(route.size() - 1) + "\n");
            }

            writer.write("------------------------------\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used for writing on an output file all the arriving times.
     * @param routes ,map containing all the routes.
     * @param parcelsPerLocation ,map containing the parcels to be delivered to each smartPoint.
     */
    private void writeArrivingTimes(HashMap<Integer, List<Integer>> routes, HashMap<Integer, ArrayList<Long>> parcelsPerLocation) {

        double[] time = new double[smartPoints];
        // arriving time at depot = 0 //
        time[0] = 0;

        for (List<Integer> route: routes.values()){

            // arriving time at 1st smartPoint of the Route = travelMatrix[0][smartPoint] //
            time[route.get(1)] = travelMatrix[0][route.get(1)];

            for (int i = 2; i < route.size() - 1; i++)
                // arriving time at smartPoint (i) = arriving time oa SmartPoint (i-1) + time to unload parcels at (i-1) + travelMatrix[i-1][i] //
                time[route.get(i)] = time[route.get(i - 1)] +  loadingCost * parcelsPerLocation.get(route.get(i-1)).size() + travelMatrix[route.get(i - 1)][route.get(i)];

        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename,true))) {

            writer.write("\n--------Arriving Times--------");

            for (int i = 1; i < smartPoints; i++ ){
                writer.write("\nSmartPoint " + i +": " + Math.round(time[i] * 1000) / 1000.0);
            }
            writer.write("\n------------------------------\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to write on an output file the transportation cost.
     * @param routes ,map containing all the routes.
     */
    private void writeTransportationCost(HashMap<Integer, List<Integer>> routes) {

        double transportationCost = 0;

        // compute the transportation cost //
        for (List<Integer> route : routes.values()){
            for (int i = 0; i < route.size() - 1; i++){
                transportationCost += runningCost * travelMatrix[route.get(i)][route.get(i+1)];
            }
        }

        // compute the vehicle cost //
        transportationCost += routes.size() * vehicleCost;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename,true))) {
            writer.write("\nTransportation Cost: " + Math.round(transportationCost * 1000) / 1000.0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Class referring to the saving of each pair of SmartPoints.
     */
    static class Saving{
        int i;
        int j;
        double saving;

        /**
         * Constructor of this class.
         * @param i ,smartPoint i.
         * @param j ,smartPoint j.
         * @param saving ,saving (i,j) = travelDistance[0][i] + travelDistance[0][j] - travelDistance[i][j].
         */
        public Saving(int i, int j, double saving) {
            this.i = i;
            this.j = j;
            this.saving = saving;
        }
    }

    /**
     * Main method.
     * @param args ,command line arguments.
     */
    public static void main(String[] args) {

        String SPFilename;
        String parcelFilename;
        String outputFilename;
        String mipProperties;
        try {
            SPFilename = args[0];
            parcelFilename = args[1];
            outputFilename = args[2];
            mipProperties = args[3];
        }catch (Exception e){
            System.out.println("Error: no given files as arguments!");
            throw new RuntimeException(e);
        }

        SAHv2 vrp = new SAHv2(SPFilename,parcelFilename,outputFilename,mipProperties);
        vrp.execute();
    }
}