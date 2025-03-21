@echo off

REM 
set PROJECT_DIRECTORY=C:\Users\Giorgos\Desktop\Thesis\src
set CPLEX_JAR=C:\Users\Giorgos\Desktop\Thesis\lib\cplex.jar
REM

cd %PROJECT_DIRECTORY%


javac -cp ".;%CPLEX_JAR%" SSAHv1.java

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/200/smartPoints200.txt ../input/20/200/parcels200.txt ../output/oneSSA/20/200/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/400/smartPoints400.txt ../input/20/400/parcels400.txt ../output/oneSSA/20/400/solution400.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/500/smartPoints500.txt ../input/20/500/parcels500.txt ../output/oneSSA/20/500/solution500.txt ../input/MIP.properties


javac -cp ".;%CPLEX_JAR%" SSAHv2.java


java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/200/smartPoints200.txt ../input/20/200/parcels200.txt ../output/multipleSSA/20/200/solution200.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/400/smartPoints400.txt ../input/20/400/parcels400.txt ../output/multipleSSA/20/400/solution400.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/500/smartPoints500.txt ../input/20/500/parcels500.txt ../output/multipleSSA/20/500/solution500.txt ../input/MIP.properties


pause