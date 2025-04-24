@echo off

REM 
set PROJECT_DIRECTORY=C:\Users\Giorgos\Desktop\Thesis\src
set CPLEX_JAR=C:\Users\Giorgos\Desktop\Thesis\lib\cplex.jar
REM

cd %PROJECT_DIRECTORY%

javac -cp ".;%CPLEX_JAR%" VRPv1.java

java -cp ".;%CPLEX_JAR%" VRPv1 ../input/5/50/smartPoints50.txt ../input/5/50/p50_1.txt ../output/oneOptimal/5/50/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv1 ../input/5/50/smartPoints50.txt ../input/5/50/p50_2.txt ../output/oneOptimal/5/50/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv1 ../input/5/50/smartPoints50.txt ../input/5/50/p50_3.txt ../output/oneOptimal/5/50/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv1 ../input/5/50/smartPoints50.txt ../input/5/50/p50_4.txt ../output/oneOptimal/5/50/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv1 ../input/5/50/smartPoints50.txt ../input/5/50/p50_5.txt ../output/oneOptimal/5/50/solution5.txt ../input/MIP.properties

javac -cp ".;%CPLEX_JAR%" VRPv2.java

java -cp ".;%CPLEX_JAR%" VRPv2 ../input/5/50/smartPoints50.txt ../input/5/50/p50_1.txt ../output/multipleOptimal/5/50/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv2 ../input/5/50/smartPoints50.txt ../input/5/50/p50_2.txt ../output/multipleOptimal/5/50/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv2 ../input/5/50/smartPoints50.txt ../input/5/50/p50_3.txt ../output/multipleOptimal/5/50/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv2 ../input/5/50/smartPoints50.txt ../input/5/50/p50_4.txt ../output/multipleOptimal/5/50/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv2 ../input/5/50/smartPoints50.txt ../input/5/50/p50_5.txt ../output/multipleOptimal/5/50/solution5.txt ../input/MIP.properties

javac -cp ".;%CPLEX_JAR%" VRPv3.java

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/5/50/smartPoints50.txt ../input/5/50/p50_1.txt ../output/oneVRP/5/50/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/5/50/smartPoints50.txt ../input/5/50/p50_2.txt ../output/oneVRP/5/50/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/5/50/smartPoints50.txt ../input/5/50/p50_3.txt ../output/oneVRP/5/50/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/5/50/smartPoints50.txt ../input/5/50/p50_4.txt ../output/oneVRP/5/50/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/5/50/smartPoints50.txt ../input/5/50/p50_5.txt ../output/oneVRP/5/50/solution5.txt ../input/MIP.properties

javac -cp ".;%CPLEX_JAR%" VRPv4.java

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/5/50/smartPoints50.txt ../input/5/50/p50_1.txt ../output/multipleVRP/5/50/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/5/50/smartPoints50.txt ../input/5/50/p50_2.txt ../output/multipleVRP/5/50/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/5/50/smartPoints50.txt ../input/5/50/p50_3.txt ../output/multipleVRP/5/50/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/5/50/smartPoints50.txt ../input/5/50/p50_4.txt ../output/multipleVRP/5/50/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/5/50/smartPoints50.txt ../input/5/50/p50_5.txt ../output/multipleVRP/5/50/solution5.txt ../input/MIP.properties

javac -cp ".;%CPLEX_JAR%" SSAHv1.java

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/5/50/smartPoints50.txt ../input/5/50/p50_1.txt ../output/oneSSA/5/50/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/5/50/smartPoints50.txt ../input/5/50/p50_2.txt ../output/oneSSA/5/50/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/5/50/smartPoints50.txt ../input/5/50/p50_3.txt ../output/oneSSA/5/50/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/5/50/smartPoints50.txt ../input/5/50/p50_4.txt ../output/oneSSA/5/50/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/5/50/smartPoints50.txt ../input/5/50/p50_5.txt ../output/oneSSA/5/50/solution5.txt ../input/MIP.properties

javac -cp ".;%CPLEX_JAR%" SSAHv2.java

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/5/50/smartPoints50.txt ../input/5/50/p50_1.txt ../output/multipleSSA/5/50/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/5/50/smartPoints50.txt ../input/5/50/p50_2.txt ../output/multipleSSA/5/50/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/5/50/smartPoints50.txt ../input/5/50/p50_3.txt ../output/multipleSSA/5/50/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/5/50/smartPoints50.txt ../input/5/50/p50_4.txt ../output/multipleSSA/5/50/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/5/50/smartPoints50.txt ../input/5/50/p50_5.txt ../output/multipleSSA/5/50/solution5.txt ../input/MIP.properties


pause