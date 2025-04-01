@echo off

REM 
set PROJECT_DIRECTORY=C:\Users\Giorgos\Desktop\Thesis\src
set CPLEX_JAR=C:\Users\Giorgos\Desktop\Thesis\lib\cplex.jar
REM

cd %PROJECT_DIRECTORY%

javac -cp ".;%CPLEX_JAR%" VRPv2.java

java -cp ".;%CPLEX_JAR%" VRPv2 ../input/example/smartPoints.txt ../input/example/parcels.txt ../output/example/solutionOptimal.txt ../input/MIP.properties

javac -cp ".;%CPLEX_JAR%" VRPv4.java

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/example/smartPoints.txt ../input/example/parcels.txt ../output/example/solutionVRP.txt ../input/MIP.properties

javac -cp ".;%CPLEX_JAR%" SSAHv2.java

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/example/smartPoints.txt ../input/example/parcels.txt ../output/example/solutionSSAH.txt ../input/MIP.properties

pause