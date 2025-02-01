import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.io.*;
import java.util.*;

/**
 * Class which solves the Optimal Vehicle Routing Problem. We have:
 *  a set of SmartPoints, each one with its capacity constraints,
 *  a set of parcels, each one with its delivery location, a penalty for not delivering it and its size
 *  a fleet of unlimited identical vehicles, with its capacity constraints
 *  constants variables for the vehicle cost, running cost, loading cost and duration of a shift
 *  a time matrix containing the travel duration between the SmartPoints in minutes
 * *
 * Each parcel has up to three possible delivery location, and the sizes of the cells are nested
 * i.e. a Small parcel can fit on a Medium or Large cell of a SmartPoint.
 */
public class VRPv2 {

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
    private double[] parcelPenalty;
    private int[][] parcelLocation;
    private int timeShift;
    private double vehicleCost;
    private double runningCost;
    private double loadingCost;
    private static final int sizes = 3;
    private static final int deliveryLocations = 3;
    private static int numberRoutes = 1;

    /**
     * Constructor of this class.
     * @param SPFilename ,contains the input data considering the Vehicle and SmartPoint parameters.
     * @param parcelFilename ,contains the input data considering the Parcel parameters.
     * @param outputFilename ,file for writing the output.
     * @param properties ,contains tuning for the LP solver.
     */
    public VRPv2(String SPFilename, String parcelFilename, String outputFilename, String properties) {
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
            parcelPenalty = new double[parcels];
            parcelLocation = new int[parcels][deliveryLocations];

            for (int i = 0; i < parcels; i++){

                String[] inputs = reader.readLine().split(" ", deliveryLocations + 3);

                parcelID[i] = Long.parseLong(inputs[0]);
                parcelSize[i] = Integer.parseInt(inputs[1]);
                parcelPenalty[i] = Double.parseDouble(inputs[2]);

                for (int j = 0; j < deliveryLocations; j++)
                    parcelLocation[i][j] = Integer.parseInt(inputs[j + 3]);
            }
        } catch (IOException e) {
            System.err.println("Error: Could not open the input File! " + e.getMessage());
        }
    }

    /**
     * Method used to formulate the MILP.
     */
    private void solve() {

        final long startTime = System.currentTimeMillis();

        ////////////////////////////////////////// Helper Structures ///////////////////////////////////////////////

        // contains max number of parcels size j a Vehicle can carry //
        int[] maxVehicleCapacity = new int[sizes];

        for (int j = 0; j < sizes; j++) {
            maxVehicleCapacity[j] = vehicleCapacity[j];
            for (int k = j + 1; k < sizes; k++)
                maxVehicleCapacity[j] += vehicleCapacity[k];
        }

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

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        try(IloCplex cplex = new IloCplex()){

            //////////////////////////////////Solver Parameters ////////////////////////////////////////////

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

            ////////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////// Setting Up Main Objective ////////////////////////////////////

            IloNumVar[][] x = new IloNumVar[smartPoints][smartPoints];

            for (int i = 0; i < smartPoints; i++)
                x[i] = cplex.boolVarArray(smartPoints);


            IloNumVar[][][] y = new IloNumVar[parcels][smartPoints][smartPoints];

            for (int q = 0; q < parcels; q++) {
                for (int i = 0; i < smartPoints; i++)
                    y[q][i] = cplex.boolVarArray(smartPoints);
            }

            IloNumVar[][] z = new IloNumVar[parcels][smartPoints];

            for (int q = 0; q < parcels; q++)
                z[q] = cplex.boolVarArray(smartPoints);

            IloNumVar[] t = cplex.numVarArray(smartPoints, 0, timeShift);

            // transportation cost of the main objective //
            IloLinearNumExpr transportationObjective = cplex.linearNumExpr();

            for (int i = 0; i < smartPoints; i++) {
                for (int j = 0; j < smartPoints; j++) {
                    if (i != j)
                        transportationObjective.addTerm(travelMatrix[i][j], x[i][j]);
                }
            }

            // vehicle cost of the main objective //
            IloLinearNumExpr vehicleObjective = cplex.linearNumExpr();
            for (int i = 1; i < smartPoints; i++)
                vehicleObjective.addTerm(vehicleCost, x[0][i]);

            // penalty cost of the main objective //
            IloNumExpr penaltyObjective = cplex.linearNumExpr();

            for (int q = 0; q < parcels; q++) {

                IloLinearNumExpr possibleLocations = cplex.linearNumExpr();
                //check its one of the three possible locations//
                for (int l = 0; l < deliveryLocations; l++) {
                    if (parcelLocation[q][l] != 0)
                        possibleLocations.addTerm(1.0, z[q][parcelLocation[q][l]]);
                }

                penaltyObjective = cplex.sum(
                        penaltyObjective,
                        cplex.prod(cplex.diff(1.0, possibleLocations), parcelPenalty[q]));
            }

            // main objective = min { transportationObjective + vehicleObjective + penaltyObjective } //
            cplex.addMinimize(cplex.sum(
                    penaltyObjective,
                    cplex.sum(cplex.prod(runningCost, transportationObjective),vehicleObjective))
            );

            ////////////////////////////////////////////////////////////////////////////////////////////////

            ////////////////////////////// Setting up all the Constraints //////////////////////////////////

            System.out.println("\n" + cplex.getObjective());

            System.out.println("\nConstraint 1: Capacity Constraint for each Vehicle moving to each SP.");

            for (int i = 1; i < smartPoints; i++) {
                for (int j = 0; j < sizes; j++) {

                    IloLinearNumExpr loadedParcels = cplex.linearNumExpr();
                    IloLinearNumExpr loadedLargerParcels = cplex.linearNumExpr();

                    for (int q = 0; q < parcelsPerSize.get(j).size(); q++)
                        loadedParcels.addTerm(1.0, y[parcelsPerSize.get(j).get(q)][0][i]);

                    for (int k = j + 1; k < sizes; k++) {
                        for (int q = 0; q < parcelsPerSize.get(k).size(); q++)
                            loadedLargerParcels.addTerm(1.0, y[parcelsPerSize.get(k).get(q)][0][i]);
                    }

                    cplex.addLe(loadedParcels, cplex.sum(
                            cplex.prod(x[0][i], maxVehicleCapacity[j]),
                            cplex.prod(-1.00, loadedLargerParcels)));
                }
            }

            System.out.println("\nConstraint 2: Capacity Constraint for each SP.");

            for (int i = 1; i < smartPoints; i++) {
                for (int j = 0; j < sizes; j++) {

                    IloLinearNumExpr delivered = cplex.linearNumExpr();
                    IloLinearNumExpr deliveredBigger = cplex.linearNumExpr();

                    for (int q = 0; q < parcelsPerSize.get(j).size(); q++) {
                        if (locationsPerParcel.get(parcelsPerSize.get(j).get(q)).contains(i))
                            delivered.addTerm(1.0, z[parcelsPerSize.get(j).get(q)][i]);
                    }

                    for (int k = j + 1; k < sizes; k++) {
                        for (int q = 0; q < parcelsPerSize.get(k).size(); q++) {
                            if (locationsPerParcel.get(parcelsPerSize.get(k).get(q)).contains(i))
                                deliveredBigger.addTerm(1.0, z[parcelsPerSize.get(k).get(q)][i]);
                        }
                    }

                    cplex.addLe(delivered, cplex.sum(maxLocationCapacity[i][j], cplex.prod(-1.00, deliveredBigger)));
                }
            }

            System.out.println("\nConstraint 3: At most one Vehicle can depart from each SP.");

            for (int i = 1; i < smartPoints; i++) {

                IloLinearNumExpr routes = cplex.linearNumExpr();

                for (int j = 0; j < smartPoints; j++) {
                    if (i != j)
                        routes.addTerm(1.0, x[i][j]);
                }

                cplex.addLe(routes, 1.0);
            }

            System.out.println("\nConstraint 4: Outgoing Vehicles from SPo i = Incoming Vehicles to SPo i.");

            for (int i = 0; i < smartPoints; i++) {

                IloLinearNumExpr outgoing = cplex.linearNumExpr();
                IloLinearNumExpr incoming = cplex.linearNumExpr();

                for (int j = 0; j < smartPoints; j++) {

                    if (i != j)
                        outgoing.addTerm(1.0, x[i][j]);
                }

                for (int j = 0; j < smartPoints; j++) {

                    if (i != j)
                        incoming.addTerm(1.0, x[j][i]);
                }

                cplex.addEq(outgoing, incoming);
            }

            System.out.println("\nConstraint 5: Each parcel that arrives at SPi != parcelLocation[i] must depart from it.");

            for (int q = 0; q < parcels; q++) {
                for (int j = 1; j < smartPoints; j++) {
                    if (!locationsPerParcel.get(q).contains(j)) {

                        IloLinearNumExpr enter = cplex.linearNumExpr();
                        IloLinearNumExpr leave = cplex.linearNumExpr();

                        for (int i = 0; i < smartPoints; i++) {
                            if (i != j)
                                enter.addTerm(1.0, y[q][i][j]);
                        }

                        for (int i = 0; i < smartPoints; i++) {
                            if (i != j)
                                leave.addTerm(1.0, y[q][j][i]);
                        }

                        cplex.addEq(enter, leave);
                    }
                }
            }

            System.out.println("\nConstraint 6: Each parcel once it arrives at SPi == parcelLocation[q][l] may be " +
                    "delivered to it.");

            for (int q = 0; q < parcels; q++) {
                for (int j = 1; j < smartPoints; j++) {
                    if (locationsPerParcel.get(q).contains(j)) {

                        IloLinearNumExpr enteringParcels = cplex.linearNumExpr();
                        IloLinearNumExpr departingParcels = cplex.linearNumExpr();

                        for (int i = 0; i < smartPoints; i++) {
                            if (i != j)
                                enteringParcels.addTerm(1.0, y[q][i][j]);
                        }

                        for (int i = 0; i < smartPoints; i++) {
                            if (i != j)
                                departingParcels.addTerm(1.0, y[q][j][i]);
                        }

                        cplex.addEq(enteringParcels, cplex.sum(departingParcels, z[q][j]));
                    }
                }
            }

            System.out.println("\nConstraint 7: Choosing if we deliver each parcel.");

            for (int q = 0; q < parcels; q++) {

                IloLinearNumExpr delivered = cplex.linearNumExpr();

                for (int l = 0; l < locationsPerParcel.get(q).size(); l++)
                    delivered.addTerm(1.0, z[q][locationsPerParcel.get(q).get(l)]);

                cplex.addLe(delivered, 1.0);
            }

            System.out.println("\nConstraint 8: Each parcel shall leave the depot only if it is to be delivered.");

            for (int q = 0; q < parcels; q++) {

                IloLinearNumExpr loaded = cplex.linearNumExpr();
                IloLinearNumExpr delivered = cplex.linearNumExpr();

                for (int i = 1; i < smartPoints; i++)
                    loaded.addTerm(1.0, y[q][0][i]);

                for (int l = 0; l < locationsPerParcel.get(q).size(); l++)
                    delivered.addTerm(1.0, z[q][locationsPerParcel.get(q).get(l)]);

                cplex.addEq(loaded, delivered);
            }

            System.out.println("\nConstraint 9: Each parcel must be loaded to a vehicle in order to move form SPi to SPj.");

            for (int q = 0; q < parcels; q++) {
                for (int i = 0; i < smartPoints; i++) {
                    for (int j = 0; j < smartPoints; j++) {
                        if (i != j)
                            cplex.addLe(y[q][i][j], x[i][j]);
                    }
                }
            }

            System.out.println("\nConstraint 10: Starting Time from depot equals to 0.");

            cplex.addEq(0, t[0]);

            System.out.println("\nConstraint 11: After arriving at each SP and unloading the parcels we must be able " +
                    "to return to depot before the end of the shift (T).");

            for (int i = 1; i < smartPoints; i++) {

                IloLinearNumExpr loadedParcels = cplex.linearNumExpr();

                for (int q = 0; q < parcels; q++) {
                    if (locationsPerParcel.get(q).contains(i))
                        loadedParcels.addTerm(1.0, z[q][i]);
                }

                cplex.addLe(cplex.sum(cplex.sum(t[i], cplex.prod(loadingCost, loadedParcels)), travelMatrix[i][0]), timeShift);
            }

            System.out.println("\nConstraint 12: After travelling to SPi from SPj, the arriving time of t[i] must be t[j] + " +
                    " time to unload the parcels at SPj + travelling time from SPj to SPi.");

            for (int i = 1; i < smartPoints; i++) {
                for (int j = 0; j < smartPoints; j++) {
                    if (i != j) {

                        IloLinearNumExpr unloadedParcels = cplex.linearNumExpr();

                        for (int q = 0; q < parcels; q++) {
                            if (locationsPerParcel.get(q).contains(j))
                                unloadedParcels.addTerm(1.0, z[q][j]);
                        }

                        cplex.addGe(t[i], cplex.sum(
                                cplex.sum(cplex.sum(cplex.prod(loadingCost, unloadedParcels), t[j]), travelMatrix[j][i]),
                                cplex.prod(cplex.prod(-1.0, cplex.diff(1.0, x[j][i])), timeShift)));
                    }
                }
            }

            System.out.println("\nConstraint 13: No reason moving from SPi to SPi.");

            for (int i = 0; i < smartPoints; i++)
                cplex.addEq(0.0, x[i][i]);

            System.out.println("\nConstraint 14: No reason moving a parcel from SPi to SPi.\n");

            for (int q = 0; q < parcels; q++) {
                for (int i = 0; i < smartPoints; i++)
                    cplex.addEq(y[q][i][i], 0.0);
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////// Solving the problem //////////////////////////////////////////

            if (cplex.solve()) {

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {

                    writer.write("Total execution time: " +
                            Math.round(((System.currentTimeMillis() - startTime) / 60000.0) * 10000) / 10000.0 +
                            " minutes.\n\n");

                    double transportationCost = 0;

                    for (int i = 0; i < smartPoints; i++){
                        for (int j = 0; j < smartPoints; j++)
                            transportationCost += cplex.getValue(x[i][j]) * travelMatrix[i][j];
                    }

                    for (int i = 1; i < smartPoints; i++)
                        transportationCost += vehicleCost * cplex.getValue(x[0][i]);

                    writer.write("Optimal solution found: " + Math.round(cplex.getObjValue() * 1000) / 1000.0 + "\n");
                    writer.write("Transportation Cost: " +  Math.round(transportationCost * 1000) / 1000.0 + "\n");
                    writer.write("Penalty Cost: " + Math.round((cplex.getObjValue() - transportationCost) * 1000 ) / 1000.0 + "\n");
                    writer.write("\nParcel Delivery Location:\n");

                    HashMap<Integer,List<Long>> parcelsPerLocation = new HashMap<>();
                    int deliveredParcels = 0;

                    for (int q = 0; q < parcels; q++) {
                        for (int j = 1; j < smartPoints; j++) {
                            if (locationsPerParcel.get(q).contains(j) && cplex.getValue(z[q][j]) == 1.00) {
                                writer.write("ID: " + parcelID[q]  + " -> " + j + "\n");
                                parcelsPerLocation.computeIfAbsent(j, k -> new ArrayList<>()).add(parcelID[q]);
                                deliveredParcels++;
                            }
                        }
                    }

                    writer.write("\nTotal number of parcels delivered: " + deliveredParcels + " / " + parcels + "\n");

                    writer.write("\nArriving time on each SP:\n");
                    for (int i = 0; i < smartPoints; i++) {
                        if (parcelsPerLocation.get(i) != null)
                            writer.write("Smart Point " + i + ": " + Math.abs(Math.ceil(cplex.getValue(t[i]))) + "\n");
                    }

                    int vehicleCounter = 0;

                    for (int i = 0; i < smartPoints; i++)
                        vehicleCounter += Math.abs(cplex.getValue(x[i][0])) == 1.0 ? 1 : 0;

                    writer.write("\nTotal number of vehicles used: " + vehicleCounter + "\n");

                } catch (IOException e) {
                    System.out.println("Error: couldn't write the output on a file!");
                    throw new RuntimeException(e);
                }

                double[][] vehicleRoutes = new double[smartPoints][smartPoints];

                for (int i = 0; i < smartPoints; i++) {
                    for (int j = 0; j < smartPoints; j++)
                        vehicleRoutes[i][j] = Math.abs(cplex.getValue(x[i][j])) ;
                }

                writeRoutes(vehicleRoutes);

                ////////////////////////////////////////////////////////////////////////////////////////////////////
            } else {
                System.out.println("No solution found.");
            }
        } catch (IloException e) {
            System.err.println("Concert exception caught: " + e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to write all the vehicle routes.
     * @param vehicleRoutes ,adjacent matrix of all the SmartPoints including the Depot.
     */
    private void writeRoutes(double[][] vehicleRoutes) {

        List<Integer> route = new ArrayList<>();
        boolean[] visited = new boolean[vehicleRoutes[0].length];

        route.add(0);
        visited[0] = true;

        dfs(0,route,visited,vehicleRoutes);
    }

    /**
     * Method used to perform a dfs search on the adjacent matrix, so we can extract all the vehicle routes.
     * Each Route starts and ends at the Depot = 0.
     * @param source ,node on which we stand at the moment.
     * @param route ,list of all the previous nodes we have visited.
     * @param visited ,boolean array storing the nodes we have already visited on a previous route.
     * @param vehicleRoutes ,adjacent matrix of SmartPoints including the Depot.
     */
    private void dfs(int source, List<Integer> route, boolean[] visited,double[][] vehicleRoutes) {

        // a route is completed if , starting from the Depot, we've visited at least one SmartPoint and ended at the Depot //
        if (source == 0 && route.size() > 1) {

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename,true))) {

                writer.write("\nRoute " + numberRoutes++ + ": " );

                for (int i = 0 ; i < route.size() - 1; i++)
                    writer.write(route.get(i) + "->");

                writer.write(route.get(route.size() - 1) + "");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        // continue the dfs from our source node//
        for (int destination = 0; destination < vehicleRoutes[source].length; destination++) {

            if (vehicleRoutes[source][destination] == 1.00 && (!visited[destination] || destination == 0)) {
                route.add(destination);

                // we mark the next visited location as true //
                boolean temp = visited[destination];
                visited[destination] = true;

                dfs(destination, route, visited, vehicleRoutes);

                // we remove the visited location to backtrack //
                route.remove(route.size() - 1);
                visited[destination] = temp;
            }
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

        VRPv2 vrp = new VRPv2(SPFilename,parcelFilename,outputFilename,mipProperties);
        vrp.execute();
    }
}