@echo off

REM
set PROJECT_DIRECTORY=C:\Users\Giorgos\Desktop\Thesis\src
set CPLEX_JAR=C:\Users\Giorgos\Desktop\Thesis\lib\cplex.jar
REM

cd %PROJECT_DIRECTORY%

javac -cp ".;%CPLEX_JAR%" VRPv3.java

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/400/smartPoints400.txt ../input/10/400/p400_1.txt ../output/oneVRP/10/400/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/400/smartPoints400.txt ../input/10/400/p400_2.txt ../output/oneVRP/10/400/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/400/smartPoints400.txt ../input/10/400/p400_3.txt ../output/oneVRP/10/400/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/400/smartPoints400.txt ../input/10/400/p400_4.txt ../output/oneVRP/10/400/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv3 ../input/10/400/smartPoints400.txt ../input/10/400/p400_5.txt ../output/oneVRP/10/400/solution5.txt ../input/MIP.properties


javac -cp ".;%CPLEX_JAR%" VRPv4.java

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/400/smartPoints400.txt ../input/10/400/p400_1.txt ../output/multipleVRP/10/400/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/400/smartPoints400.txt ../input/10/400/p400_2.txt ../output/multipleVRP/10/400/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/400/smartPoints400.txt ../input/10/400/p400_3.txt ../output/multipleVRP/10/400/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/400/smartPoints400.txt ../input/10/400/p400_4.txt ../output/multipleVRP/10/400/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" VRPv4 ../input/10/400/smartPoints400.txt ../input/10/400/p400_5.txt ../output/multipleVRP/10/400/solution5.txt ../input/MIP.properties


pause