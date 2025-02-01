@echo off

REM 
set PROJECT_DIRECTORY=C:\Users\Giorgos\Desktop\VRP\src
set CPLEX_JAR=C:\Users\Giorgos\Desktop\VRP\lib\cplex.jar
REM

cd %PROJECT_DIRECTORY%

javac -cp ".;%CPLEX_JAR%" VRPv3.java

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/5/smartpoints100.txt ../input/5/parcels50.txt ../output/oneVRP/5/solution50.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/5/smartpoints100.txt ../input/5/parcels100.txt ../output/oneVRP/5/solution100.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/5/smartpoints100.txt ../input/5/parcels150.txt ../output/oneVRP/5/solution150.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/5/smartpoints200.txt ../input/5/parcels200.txt ../output/oneVRP/5/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/smartpoints200.txt ../input/10/parcels200.txt ../output/oneVRP/10/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/smartpoints400.txt ../input/10/parcels400.txt ../output/oneVRP/10/solution400.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/smartpoints500.txt ../input/10/parcels500.txt ../output/oneVRP/10/solution500.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/20/smartpoints500.txt ../input/20/parcels200.txt ../output/oneVRP/20/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/20/smartpoints500.txt ../input/20/parcels400.txt ../output/oneVRP/20/solution400.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/20/smartpoints500.txt ../input/20/parcels500.txt ../output/oneVRP/20/solution500.txt ../input/MIP.properties



javac -cp ".;%CPLEX_JAR%" VRPv4.java

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/5/smartpoints100.txt ../input/5/parcels50.txt ../output/multipleVRP/5/solution50.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/5/smartpoints100.txt ../input/5/parcels100.txt ../output/multipleVRP/5/solution100.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/5/smartpoints100.txt ../input/5/parcels150.txt ../output/multipleVRP/5/solution150.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/5/smartpoints200.txt ../input/5/parcels200.txt ../output/multipleVRP/5/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/smartpoints200.txt ../input/10/parcels200.txt ../output/multipleVRP/10/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/smartpoints400.txt ../input/10/parcels400.txt ../output/multipleVRP/10/solution400.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/smartpoints500.txt ../input/10/parcels500.txt ../output/multipleVRP/10/solution500.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/20/smartpoints500.txt ../input/20/parcels200.txt ../output/multipleVRP/20/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/20/smartpoints500.txt ../input/20/parcels400.txt ../output/multipleVRP/20/solution400.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/20/smartpoints500.txt ../input/20/parcels500.txt ../output/multipleVRP/20/solution500.txt ../input/MIP.properties


pause