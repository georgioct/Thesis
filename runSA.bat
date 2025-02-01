@echo off

REM 
set PROJECT_DIRECTORY=C:\Users\Giorgos\Desktop\Thesis\src
set CPLEX_JAR=C:\Users\Giorgos\Desktop\Thesis\lib\cplex.jar
REM

cd %PROJECT_DIRECTORY%

javac -cp ".;%CPLEX_JAR%" SAHv1.java

java -cp ".;%CPLEX_JAR%" SAHv1 ../input/5/smartpoints100.txt ../input/5/parcels50.txt ../output/oneSA/5/solution50.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv1 ../input/5/smartpoints100.txt ../input/5/parcels100.txt ../output/oneSA/5/solution100.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv1 ../input/5/smartpoints100.txt ../input/5/parcels150.txt ../output/oneSA/5/solution150.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv1 ../input/5/smartpoints200.txt ../input/5/parcels200.txt ../output/oneSA/5/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv1 ../input/10/smartpoints200.txt ../input/10/parcels200.txt ../output/oneSA/10/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv1 ../input/10/smartpoints400.txt ../input/10/parcels400.txt ../output/oneSA/10/solution400.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv1 ../input/10/smartpoints500.txt ../input/10/parcels500.txt ../output/oneSA/10/solution500.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv1 ../input/20/smartpoints500.txt ../input/20/parcels200.txt ../output/oneSA/20/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv1 ../input/20/smartpoints500.txt ../input/20/parcels400.txt ../output/oneSA/20/solution400.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv1 ../input/20/smartpoints500.txt ../input/20/parcels500.txt ../output/oneSA/20/solution500.txt ../input/MIP.properties



javac -cp ".;%CPLEX_JAR%" SAHv2.java

java -cp ".;%CPLEX_JAR%" SAHv2 ../input/5/smartpoints100.txt ../input/5/parcels50.txt ../output/multipleSA/5/solution50.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv2 ../input/5/smartpoints100.txt ../input/5/parcels100.txt ../output/multipleSA/5/solution100.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv2 ../input/5/smartpoints100.txt ../input/5/parcels150.txt ../output/multipleSA/5/solution150.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv2 ../input/5/smartpoints200.txt ../input/5/parcels200.txt ../output/multipleSA/5/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv2 ../input/10/smartpoints200.txt ../input/10/parcels200.txt ../output/multipleSA/10/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv2 ../input/10/smartpoints400.txt ../input/10/parcels400.txt ../output/multipleSA/10/solution400.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv2 ../input/10/smartpoints500.txt ../input/10/parcels500.txt ../output/multipleSA/10/solution500.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv2 ../input/20/smartpoints500.txt ../input/20/parcels200.txt ../output/multipleSA/20/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv2 ../input/20/smartpoints500.txt ../input/20/parcels400.txt ../output/multipleSA/20/solution400.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SAHv2 ../input/20/smartpoints500.txt ../input/20/parcels500.txt ../output/multipleSA/20/solution500.txt ../input/MIP.properties


pause