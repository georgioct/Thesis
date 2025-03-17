#!/bin/bash

# Define variables
PROJECT_DIRECTORY="/home/giorgos/Thesis/src"
CPLEX_JAR="/home/giorgos/Thesis/lib/cplex.jar"

# Change directory to project folder
cd "$PROJECT_DIRECTORY" || exit

# Compile Java files
javac -cp ".:$CPLEX_JAR" SAHv1.java

# Run SAHv1 with different input files
java -cp ".:$CPLEX_JAR" SAHv1 ../input/5/smartpoints100.txt ../input/5/parcels50.txt ../output/linuxOutput/5/solution50.txt ../input/MIP.properties
java -cp ".:$CPLEX_JAR" SAHv1 ../input/5/smartpoints100.txt ../input/5/parcels100.txt ../output/linuxOutput/5/solution100.txt ../input/MIP.properties
java -cp ".:$CPLEX_JAR" SAHv1 ../input/5/smartpoints100.txt ../input/5/parcels150.txt ../output/linuxOutput/5/solution150.txt ../input/MIP.properties
java -cp ".:$CPLEX_JAR" SAHv1 ../input/5/smartpoints200.txt ../input/5/parcels200.txt ../output/linuxOutput/5/solution200.txt ../input/MIP.properties
java -cp ".:$CPLEX_JAR" SAHv1 ../input/10/smartpoints200.txt ../input/10/parcels200.txt ../output/linuxOutput/10/solution200.txt ../input/MIP.properties
java -cp ".:$CPLEX_JAR" SAHv1 ../input/10/smartpoints400.txt ../input/10/parcels400.txt ../output/linuxOutput/10/solution400.txt ../input/MIP.properties
java -cp ".:$CPLEX_JAR" SAHv1 ../input/10/smartpoints500.txt ../input/10/parcels500.txt ../output/linuxOutput/10/solution500.txt ../input/MIP.properties
java -cp ".:$CPLEX_JAR" SAHv1 ../input/20/smartpoints500.txt ../input/20/parcels200.txt ../output/linuxOutput/20/solution200.txt ../input/MIP.properties
java -cp ".:$CPLEX_JAR" SAHv1 ../input/20/smartpoints500.txt ../input/20/parcels400.txt ../output/linuxOutput/20/solution400.txt ../input/MIP.properties
java -cp ".:$CPLEX_JAR" SAHv1 ../input/20/smartpoints500.txt ../input/20/parcels500.txt ../output/linuxOutput/20/solution500.txt ../input/MIP.properties

# Keep the terminal open
read -p "Press any key to continue..."