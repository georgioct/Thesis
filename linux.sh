#!/bin/bash
#SBATCH --job-name=parcelScheduling
#SBATCH --partition=batch
#SBATCH --ntasks=1
#SBATCH --time=01:10:00

# Load all the required modules
module load gcc/13.2.0-iqpfkya
module load openjdk/17.0.8.1_1-5zo2q5g
module load cplex

# Define absolute paths
PROJECT_DIRECTORY="/home/g/georgioct/Thesis/src"
CPLEX_JAR="/mnt/apps/prebuilt/ibm/ILOG/CPLEX_Studio_Community/22.1.2/cplex/lib/cplex.jar"
CPLEX_LIB_PATH="/mnt/apps/prebuilt/ibm/ILOG/CPLEX_Studio_Community/22.1.2/cplex/bin/x86-64_linux"
INPUT_DIRECTORY="/home/g/georgioct/Thesis/input"
OUTPUT_DIRECTORY="/home/g/georgioct/Thesis/output"

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

javac -cp ".:$CPLEX_JAR" VRPv1.java

java -Djava.library.path="$CPLEX_LIB_PATH" -cp ".:$CPLEX_JAR" VRPv1 "$INPUT_DIRECTORY/5/50/smartPoints50.txt" "$INPUT_DIRECTORY/5/50/p50.txt" "$OUTPUT_DIRECTORY/oneSA/5/50/solution50.txt" "$INPUT_DIRECTORY/MIP.properties"