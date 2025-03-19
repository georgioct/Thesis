@echo off

REM
set PROJECT_DIRECTORY=C:\Users\Giorgos\Desktop\Thesis\src
set CPLEX_JAR=C:\Users\Giorgos\Desktop\Thesis\lib\cplex.jar
REM

cd %PROJECT_DIRECTORY%

javac -cp ".;%CPLEX_JAR%" VRPv1.java

java -cp ".;%CPLEX_JAR%" VRPv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_1.txt ../output/oneOptimal/10/200/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_2.txt ../output/oneOptimal/10/200/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_3.txt ../output/oneOptimal/10/200/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_4.txt ../output/oneOptimal/10/200/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_5.txt ../output/oneOptimal/10/200/solution5.txt ../input/MIP.properties


javac -cp ".;%CPLEX_JAR%" VRPv2.java

java -cp ".;%CPLEX_JAR%" VRPv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_1.txt ../output/multipleOptimal/10/200/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_2.txt ../output/multipleOptimal/10/200/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_3.txt ../output/multipleOptimal/10/200/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_4.txt ../output/multipleOptimal/10/200/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_5.txt ../output/multipleOptimal/10/200/solution5.txt ../input/MIP.properties


javac -cp ".;%CPLEX_JAR%" VRPv3.java

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/200/smartPoints200.txt ../input/10/200/p200_1.txt ../output/oneVRP/10/200/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/200/smartPoints200.txt ../input/10/200/p200_2.txt ../output/oneVRP/10/200/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/200/smartPoints200.txt ../input/10/200/p200_3.txt ../output/oneVRP/10/200/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/200/smartPoints200.txt ../input/10/200/p200_4.txt ../output/oneVRP/10/200/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/200/smartPoints200.txt ../input/10/200/p200_5.txt ../output/oneVRP/10/200/solution5.txt ../input/MIP.properties


javac -cp ".;%CPLEX_JAR%" VRPv4.java

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/200/smartPoints200.txt ../input/10/200/p200_1.txt ../output/multipleVRP/10/200/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/200/smartPoints200.txt ../input/10/200/p200_2.txt ../output/multipleVRP/10/200/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/200/smartPoints200.txt ../input/10/200/p200_3.txt ../output/multipleVRP/10/200/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/200/smartPoints200.txt ../input/10/200/p200_4.txt ../output/multipleVRP/10/200/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/200/smartPoints200.txt ../input/10/200/p200_5.txt ../output/multipleVRP/10/200/solution5.txt ../input/MIP.properties


javac -cp ".;%CPLEX_JAR%" SSAHv1.java

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_1.txt ../output/oneSSA/10/200/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_2.txt ../output/oneSSA/10/200/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_3.txt ../output/oneSSA/10/200/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_4.txt ../output/oneSSA/10/200/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_5.txt ../output/oneSSA/10/200/solution5.txt ../input/MIP.properties


javac -cp ".;%CPLEX_JAR%" SSAHv2.java

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_1.txt ../output/multipleSSA/10/200/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_2.txt ../output/multipleSSA/10/200/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_3.txt ../output/multipleSSA/10/200/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_4.txt ../output/multipleSSA/10/200/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_5.txt ../output/multipleSSA/10/200/solution5.txt ../input/MIP.properties

pause