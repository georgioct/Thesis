#!/bin/bash

# Define absolute paths
PROJECT_DIRECTORY="/home/giorgos/Thesis/src"
CPLEX_JAR="/home/giorgos/cplex/cplex/lib/cplex.jar"
CPLEX_LIB_PATH="/home/giorgos/cplex/cplex/bin/linux_x86_64.bin"
INPUT_DIRECTORY="/home/giorgos/Thesis/input"
OUTPUT_DIRECTORY="/home/giorgos/Thesis/output"

# CPLEX JAR exists #
if [ ! -f "$CPLEX_JAR" ]; then
    echo "Error: CPLEX JAR not found at $CPLEX_JAR"
    exit 1
fi

# CPLEX native library path exists #
if [ ! -d "$CPLEX_LIB_PATH" ]; then
    echo "Error: CPLEX native library path not found at $CPLEX_LIB_PATH"
    exit 1
fi

# Change to project directory #
cd "$PROJECT_DIRECTORY"

# Compile Java files #
javac -cp ".:$CPLEX_JAR" SAHv1.java

# Run SAHv1 #
java -Djava.library.path="$CPLEX_LIB_PATH" -cp ".:$CPLEX_JAR" SAHv1 "$INPUT_DIRECTORY/5/50/smartPoints50.txt" "$INPUT_DIRECTORY/5/50/p50.txt" "$OUTPUT_DIRECTORY/oneSA/5/50/solution50.txt" "$INPUT_DIRECTORY/MIP.properties"

read -p "Press any key to continue..."