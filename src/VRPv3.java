import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Class which solves the Semi-Optimal Vehicle Routing Problem. We have:
 *  a set of SmartPoints, each one with its capacity constraints,
 *  a set of parcels, each one with its delivery location, a penalty for not delivering it and its size
 *  a fleet of unlimited identical vehicles, with its capacity constraints
 *  constants variables for the vehicle cost, running cost, loading cost and duration of a shift
 *  a time matrix containing the travel duration between the SmartPoints in minutes
 * *
 * By solving an LP program we get the max number of parcels we can deliver and their delivery locations ->
 * we reduce the decision variables from 3 to 2 -> we formulate the main problem as a pure VRP problem.
 * *
 * Each parcel has one delivery location, and the sizes of the cells are nested i.e. a Small parcel can fit on
 * a Medium or Large cell of a SmartPoint.
 */
public class VRPv3 {

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
    private int[] parcelLocation;
    private int timeShift;
    private double vehicleCost;
    private double runningCost;
    private double loadingCost;
    private static final int sizes = 3;
    private static final int deliveryLocations = 3;
    private int numberRoutes = 1;

    /**
     * Constructor of this class.
     * @param SPFilename ,contains the input data considering the Vehicle and SmartPoint parameters.
     * @param parcelFilename ,contains the input data considering the Parcel parameters.
     * @param outputFilename ,file for writing the output.
     * @param properties ,contains tuning for the LP solver.
     */
    public VRPv3(String SPFilename, String parcelFilename, String outputFilename, String properties) {
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
        double[][] delivery = solveDelivery();
        solveVRP(delivery);
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
            parcelLocation = new int[parcels];

            for (int i = 0; i < parcels; i++){

                String[] inputs = reader.readLine().split(" ", deliveryLocations + 3);

                parcelID[i] = Long.parseLong(inputs[0]);
                parcelSize[i] = Integer.parseInt(inputs[1]);
                parcelLocation[i] = Integer.parseInt(inputs[3]);
            }
        } catch (IOException e) {
            System.err.println("Error: Could not open the input File! " + e.getMessage());
        }
    }

    /**
     * Method that solves the LP problem referring to the delivery location of the parcels.
     * The solver returns matrix ( parcels x smartPoints ) matrix, where
     * matrix[i][j] = 1 --> parcel i will be delivered to smartPoint j
     * It maximizes the number of delivered parcels.
     * @return matrix[parcels][smartPoints] containing the delivery location of each parcel.
     */
    private double[][] solveDelivery() {

        // contains the delivery location for each parcel  //
        double[][] matrix = new double[parcels][smartPoints];

        ///////////////////////////////////////// Helper Structures ////////////////////////////////////////////

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

        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        try (IloCplex cplex = new IloCplex()) {

            //////////////////////////////////Solver Parameters ////////////////////////////////////////////////

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

            /////////////////////////////////// Setting Up Main Objective //////////////////////////////////////

            IloNumVar[][] delivery = new IloNumVar[parcels][smartPoints];

            for (int q = 0; q < parcels; q++)
                delivery[q] = cplex.boolVarArray(smartPoints);

            IloLinearNumExpr deliveryObjective = cplex.linearNumExpr();

            // sums up all the delivered parcels //
            for (int q = 0; q < parcels; q++)
                deliveryObjective.addTerm(1.00,delivery[q][parcelLocation[q]]);

            // main objective = max { deliveryObjective } //
            cplex.addMaximize(deliveryObjective);

            ////////////////////////////////////////////////////////////////////////////////////////////////////

            //////////////////////////////// Setting up all the Constraints ////////////////////////////////////

            System.out.println("\n" + cplex.getObjective());

            System.out.println("\nConstraint 1: Capacity Constraint for each SP.");

            for (int i = 1; i < smartPoints; i++) {
                for (int j = 0; j < sizes; j++) {

                    IloLinearNumExpr delivered = cplex.linearNumExpr();
                    IloLinearNumExpr deliveredBigger = cplex.linearNumExpr();

                    for (int q = 0; q < parcelsPerSize.get(j).size(); q++) {
                        if (parcelLocation[parcelsPerSize.get(j).get(q)] == i)
                            delivered.addTerm(1.0, delivery[parcelsPerSize.get(j).get(q)][i]);
                    }

                    for (int k = j + 1; k < sizes; k++) {
                        for (int q = 0; q < parcelsPerSize.get(k).size(); q++) {
                            if (parcelLocation[parcelsPerSize.get(k).get(q)] == i)
                                deliveredBigger.addTerm(1.0, delivery[parcelsPerSize.get(k).get(q)][i]);
                        }
                    }

                    cplex.addLe(delivered,
                            cplex.sum(
                                    maxLocationCapacity[i][j],
                                    cplex.prod(-1.00, deliveredBigger)
                            )
                    );
                }
            }

            System.out.println("\nConstraint 2: Choosing if we deliver each parcel.\n");

            for (int q = 0; q < parcels; q++)
                cplex.addLe(delivery[q][parcelLocation[q]], 1.0);

            ////////////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////// Solving the problem //////////////////////////////////////////

            if (cplex.solve()) {

                for (int q = 0; q < parcels; q++){
                    for (int i = 0; i < smartPoints; i++)
                        if ( parcelLocation[q] == i)
                            matrix[q][i] = Math.abs(cplex.getValue(delivery[q][i]));
                        else
                            matrix[q][i] = 0.0;
                }
            } else {
                System.out.println("No solution found.");
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////
        } catch (IloException | IOException e) {
            System.err.println("Concert exception caught: " + e);
            throw new RuntimeException(e);
        }

        return matrix;
    }

    /**
     * Methods that solves the VRP problem.
     * @param delivery, matrix containing the delivery location of all parcels.
     */
    private void solveVRP(double[][] delivery) {

        final long startTime = System.currentTimeMillis();

        //////////////////////////////////////// Helper Structures /////////////////////////////////////////////

        // contains max number of parcels size j a Vehicle can carry //
        int[] maxVehicleCapacity = new int[sizes];

        for (int j = 0; j < sizes; j++) {
            maxVehicleCapacity[j] = vehicleCapacity[j];
            for (int k = j + 1; k < sizes; k++)
                maxVehicleCapacity[j] += vehicleCapacity[k];
        }

        // contains a list of all the parcels size j //
        ArrayList<ArrayList<Integer>> parcelsPerSize = new ArrayList<>(new ArrayList<>());

        // initialization //
        for (int i = 0; i < sizes; i++)
            parcelsPerSize.add(new ArrayList<>());

        for (int q = 0; q < parcels; q++)
            parcelsPerSize.get(parcelSize[q]).add(q);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        try (IloCplex cplex = new IloCplex()) {

            ////////////////////////////////////////Solver Parameters //////////////////////////////////////////////

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

            ////////////////////////////////////////////////////////////////////////////////////////////////////////

            //////////////////////////////////// Setting Up Main Objective /////////////////////////////////////////

            IloNumVar[][] route = new IloNumVar[smartPoints][smartPoints];

            for (int i = 0; i < smartPoints; i++)
                route[i] = cplex.boolVarArray(smartPoints);


            IloNumVar[][][] parcelRoute = new IloNumVar[parcels][smartPoints][smartPoints];

            for (int q = 0; q < parcels; q++) {
                for (int i = 0; i < smartPoints; i++)
                    parcelRoute[q][i] = cplex.boolVarArray(smartPoints);
            }

            IloNumVar[] time = cplex.numVarArray(smartPoints, 0, timeShift);

            // transportation cost of the main objective //
            IloLinearNumExpr transportationObjective = cplex.linearNumExpr();

            for (int i = 0; i < smartPoints; i++) {
                for (int j = 0; j < smartPoints; j++) {
                    if (i != j)
                        transportationObjective.addTerm(travelMatrix[i][j], route[i][j]);
                }
            }

            // vehicle cost of the main objective //
            IloLinearNumExpr vehicleObjective = cplex.linearNumExpr();

            for (int i = 1; i < smartPoints; i++)
                vehicleObjective.addTerm(vehicleCost, route[0][i]);


            // main objective = min { transportationObjective + vehicleObjective } //
            cplex.addMinimize(cplex.sum(cplex.prod(runningCost, transportationObjective),vehicleObjective));

            ////////////////////////////////////////////////////////////////////////////////////////////////////////

            ////////////////////////////////// Setting up all the Constraints //////////////////////////////////////

            System.out.println("\n" + cplex.getObjective());

            System.out.println("\nConstraint 1: Capacity Constraint for each Vehicle moving to each SP.");

            for (int i = 1; i < smartPoints; i++) {
                for (int j = 0; j < sizes; j++) {

                    IloLinearNumExpr loadedParcels = cplex.linearNumExpr();
                    IloLinearNumExpr loadedLargerParcels = cplex.linearNumExpr();

                    for (int q = 0; q < parcelsPerSize.get(j).size(); q++)
                        loadedParcels.addTerm(1.0, parcelRoute[parcelsPerSize.get(j).get(q)][0][i]);

                    for (int k = j + 1; k < sizes; k++) {
                        for (int q = 0; q < parcelsPerSize.get(k).size(); q++)
                            loadedLargerParcels.addTerm(1.0, parcelRoute[parcelsPerSize.get(k).get(q)][0][i]);
                    }

                    cplex.addLe(loadedParcels, cplex.sum(
                            cplex.prod(route[0][i], maxVehicleCapacity[j]),
                            cplex.prod(-1.00, loadedLargerParcels)));
                }
            }


            System.out.println("\nConstraint 2: At most one Vehicle can depart from each SP.");

            for (int i = 1; i < smartPoints; i++) {

                IloLinearNumExpr routes = cplex.linearNumExpr();

                for (int j = 0; j < smartPoints; j++) {
                    if (i != j)
                        routes.addTerm(1.0, route[i][j]);
                }

                cplex.addLe(routes, 1.0);
            }

            System.out.println("\nConstraint 3: Outgoing Vehicles from SPo i = Incoming Vehicles to SPo i.");

            for (int i = 0; i < smartPoints; i++) {

                IloLinearNumExpr outgoing = cplex.linearNumExpr();
                IloLinearNumExpr incoming = cplex.linearNumExpr();

                for (int j = 0; j < smartPoints; j++) {
                    if (i != j)
                        outgoing.addTerm(1.0, route[i][j]);
                }

                for (int j = 0; j < smartPoints; j++) {
                    if (i != j)
                        incoming.addTerm(1.0, route[j][i]);
                }

                cplex.addEq(outgoing, incoming);
            }

            System.out.println("\nConstraint 4: Each parcel that arrives at SPi != parcelLocation[i] must depart from it.");

            for (int q = 0; q < parcels; q++) {
                for (int j = 1; j < smartPoints; j++) {
                    if (parcelLocation[q] != j) {

                        IloLinearNumExpr enter = cplex.linearNumExpr();
                        IloLinearNumExpr leave = cplex.linearNumExpr();

                        for (int i = 0; i < smartPoints; i++) {
                            if (i != j)
                                enter.addTerm(1.0, parcelRoute[q][i][j]);
                        }

                        for (int i = 0; i < smartPoints; i++) {
                            if (i != j)
                                leave.addTerm(1.0, parcelRoute[q][j][i]);
                        }

                        cplex.addEq(enter, leave);
                    }
                }
            }

            System.out.println("\nConstraint 5: Each parcel once it arrives at SPi == parcelLocation[i] must be delivered to it.");

            for (int q = 0; q < parcels; q++) {
                for (int j = 1; j < smartPoints; j++) {
                    if (parcelLocation[q] == j) {

                        IloLinearNumExpr enter = cplex.linearNumExpr();
                        IloLinearNumExpr leave = cplex.linearNumExpr();

                        for (int i = 0; i < smartPoints; i++) {
                            if (i != j)
                                enter.addTerm(1.0, parcelRoute[q][i][j]);
                        }

                        for (int i = 0; i < smartPoints; i++) {
                            if (i != j)
                                leave.addTerm(1.0, parcelRoute[q][j][i]);
                        }

                        cplex.addEq(enter, delivery[q][parcelLocation[q]]);
                        cplex.addEq(leave, 0.0);
                    }
                }
            }


            System.out.println("\nConstraint 6: Each parcel shall leave the depot only if it is to be delivered.");

            for (int q = 0; q < parcels; q++) {

                IloLinearNumExpr loaded = cplex.linearNumExpr();

                for (int i = 1; i < smartPoints; i++)
                    loaded.addTerm(1.0, parcelRoute[q][0][i]);

                cplex.addEq(loaded, delivery[q][parcelLocation[q]]);
            }

            System.out.println("\nConstraint 7: Each parcel must be loaded to a vehicle in order to move form SPi to SPj.");

            for (int q = 0; q < parcels; q++) {
                for (int i = 0; i < smartPoints; i++) {
                    for (int j = 0; j < smartPoints; j++) {
                        if (i != j)
                            cplex.addLe(parcelRoute[q][i][j], route[i][j]);
                    }
                }
            }

            System.out.println("\nConstraint 8: Starting Time from depot equals to 0.");

            cplex.addEq(0, time[0]);

            System.out.println("\nConstraint 9: After arriving at each SP and unloading the parcels we must be able to return to depot " +
                    "before the end of the shift (T).");

            for (int i = 1; i < smartPoints; i++) {

                double loadedParcels = 0;

                for (int q = 0; q < parcels; q++) {
                    if (parcelLocation[q] == i)
                        loadedParcels += delivery[q][i];
                }

                cplex.addLe(cplex.sum(cplex.sum(time[i], loadingCost * loadedParcels), travelMatrix[i][0]), timeShift);
            }

            System.out.println("\nConstraint 10: After travelling to SPi from SPj, the arriving time of t[i] must be t[j] + " +
                    " time to unload the parcels at SPj + travelling time from SPj to SPi.");

            for (int i = 1; i < smartPoints; i++) {
                for (int j = 0; j < smartPoints; j++) {
                    if (i != j) {

                        double loadedParcels = 0;

                        for (int q = 0; q < parcels; q++)
                            loadedParcels += delivery[q][j];

                        cplex.addGe(time[i], cplex.sum(
                                cplex.sum(cplex.sum(loadingCost * loadedParcels, time[j]), travelMatrix[j][i]),
                                cplex.prod(cplex.prod(-1.0, cplex.diff(1.0, route[j][i])), timeShift)));
                    }
                }
            }

            System.out.println("\nConstraint 11: No reason moving from SPi to SPi.");

            for (int i = 0; i < smartPoints; i++)
                cplex.addEq(0.0, route[i][i]);

            System.out.println("\nConstraint 12: No reason moving a parcel from SPi to SPi.\n");

            for (int q = 0; q < parcels; q++) {
                for (int i = 0; i < smartPoints; i++)
                    cplex.addEq(parcelRoute[q][i][i], 0.0);
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////

            ////////////////////////////////////// Solving the problem /////////////////////////////////////////////

            if (cplex.solve()) {

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {

                    writer.write("Total execution time: " +
                            Math.round(((System.currentTimeMillis() - startTime) / 60000.0) * 10000) / 10000.0  +
                            " minutes.\n\n");

                    writer.write("Transportation Cost: " + Math.round(cplex.getObjValue() * 1000) / 1000.0 + "\n");

                    writer.write("\nParcel Delivery Location:\n");

                    HashMap<Integer,List<Long>> parcelsPerLocation = new HashMap<>();
                    int deliveredParcels = 0;

                    for (int q = 0; q < parcels; q++) {
                        if (delivery[q][parcelLocation[q]] == 1.00) {
                            writer.write("ID: " + parcelID[q] + " -> " + parcelLocation[q] + "\n");
                            parcelsPerLocation.computeIfAbsent(parcelLocation[q], k -> new ArrayList<>()).add(parcelID[q]);
                            deliveredParcels++;
                        }
                    }

                    writer.write("\nTotal number of parcels delivered: " + deliveredParcels + " / " + parcels + "\n");

                    writer.write("\nArriving time on each SP:\n");
                    for (int i = 0; i < smartPoints; i++) {
                        if (parcelsPerLocation.get(i) != null)
                            writer.write("Smart Point " + i + ": " + Math.abs(Math.ceil(cplex.getValue(time[i]))) + "\n");
                    }

                    int vehicleCounter = 0;

                    for (int i = 0; i < smartPoints; i++)
                        vehicleCounter += Math.abs(cplex.getValue(route[i][0])) == 1.0 ? 1 : 0;

                    writer.write("\nTotal number of vehicles used: " + vehicleCounter + "\n");

                } catch (IOException e) {
                    System.out.println("Error: couldn't write the output on a file!");
                    throw new RuntimeException(e);
                }

                double[][] vehicleRoutes = new double[smartPoints][smartPoints];

                for (int i = 0; i < smartPoints; i++) {
                    for (int j = 0; j < smartPoints; j++)
                        vehicleRoutes[i][j] = Math.abs(cplex.getValue(route[i][j]));
                }

                writeRoutes(vehicleRoutes);

                ////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        String outputFile;
        String mipProperties;

        try {
            SPFilename = args[0];
            parcelFilename = args[1];
            outputFile = args[2];
            mipProperties = args[3];

        }catch (Exception e){
            System.out.println("Error: no given files as arguments!");
            throw new RuntimeException(e);
        }

        VRPv3 vrp = new VRPv3(SPFilename,parcelFilename,outputFile,mipProperties);
        vrp.execute();
    }
}