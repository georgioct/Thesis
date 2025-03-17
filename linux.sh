#!/bin/bash

# Define absolute paths
PROJECT_DIRECTORY="/home/giorgos/Thesis/src"
CPLEX_JAR="/home/giorgos/Thesis/lib/cplex.jar"
INPUT_DIRECTORY="/home/giorgos/Thesis/input"
OUTPUT_DIRECTORY="/home/giorgos/Thesis/output"

# Change to project directory
cd "$PROJECT_DIRECTORY" || { echo "Error: Project directory not found"; exit 1; }

# Compile Java files
javac -cp ".:$CPLEX_JAR" SAHv1.java

# Run SAHv1 with absolute paths
java -cp ".:$CPLEX_JAR" SAHv1 "$INPUT_DIRECTORY/5/smartpoints100.txt" "$INPUT_DIRECTORY/5/parcels50.txt" "$OUTPUT_DIRECTORY/linuxOutput/5/solution50.txt" "$INPUT_DIRECTORY/MIP.properties"
java -cp ".:$CPLEX_JAR" SAHv1 "$INPUT_DIRECTORY/5/smartpoints100.txt" "$INPUT_DIRECTORY/5/parcels100.txt" "$OUTPUT_DIRECTORY/linuxOutput/5/solution100.txt" "$INPUT_DIRECTORY/MIP.properties"
java -cp ".:$CPLEX_JAR" SAHv1 "$INPUT_DIRECTORY/5/smartpoints100.txt" "$INPUT_DIRECTORY/5/parcels150.txt" "$OUTPUT_DIRECTORY/linuxOutput/5/solution150.txt" "$INPUT_DIRECTORY/MIP.properties"
java -cp ".:$CPLEX_JAR" SAHv1 "$INPUT_DIRECTORY/5/smartpoints200.txt" "$INPUT_DIRECTORY/5/parcels200.txt" "$OUTPUT_DIRECTORY/linuxOutput/5/solution200.txt" "$INPUT_DIRECTORY/MIP.properties"
java -cp ".:$CPLEX_JAR" SAHv1 "$INPUT_DIRECTORY/10/smartpoints200.txt" "$INPUT_DIRECTORY/10/parcels200.txt" "$OUTPUT_DIRECTORY/linuxOutput/10/solution200.txt" "$INPUT_DIRECTORY/MIP.properties"
java -cp ".:$CPLEX_JAR" SAHv1 "$INPUT_DIRECTORY/10/smartpoints400.txt" "$INPUT_DIRECTORY/10/parcels400.txt" "$OUTPUT_DIRECTORY/linuxOutput/10/solution400.txt" "$INPUT_DIRECTORY/MIP.properties"
java -cp ".:$CPLEX_JAR" SAHv1 "$INPUT_DIRECTORY/10/smartpoints500.txt" "$INPUT_DIRECTORY/10/parcels500.txt" "$OUTPUT_DIRECTORY/linuxOutput/10/solution500.txt" "$INPUT_DIRECTORY/MIP.properties"
java -cp ".:$CPLEX_JAR" SAHv1 "$INPUT_DIRECTORY/20/smartpoints500.txt" "$INPUT_DIRECTORY/20/parcels200.txt" "$OUTPUT_DIRECTORY/linuxOutput/20/solution200.txt" "$INPUT_DIRECTORY/MIP.properties"
java -cp ".:$CPLEX_JAR" SAHv1 "$INPUT_DIRECTORY/20/smartpoints500.txt" "$INPUT_DIRECTORY/20/parcels400.txt" "$OUTPUT_DIRECTORY/linuxOutput/20/solution400.txt" "$INPUT_DIRECTORY/MIP.properties"
java -cp ".:$CPLEX_JAR" SAHv1 "$INPUT_DIRECTORY/20/smartpoints500.txt" "$INPUT_DIRECTORY/20/parcels500.txt" "$OUTPUT_DIRECTORY/linuxOutput/20/solution500.txt" "$INPUT_DIRECTORY/MIP.properties"

# Keep terminal open
read -p "Press any key to continue..."
