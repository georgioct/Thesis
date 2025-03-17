@echo off

REM
set PROJECT_DIRECTORY=C:\Users\Giorgos\Desktop\Thesis\src
set CPLEX_JAR=C:\Users\Giorgos\Desktop\Thesis\lib\cplex.jar
REM

cd %PROJECT_DIRECTORY%

javac -cp ".;%CPLEX_JAR%" SSAHv1.java

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/5/50/smartPoints50.txt ../input/5/50/p50.txt ../output/oneSSA/5/50/solution50.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/5/100/smartPoints100.txt ../input/5/100/p100.txt ../output/oneSSA/5/100/solution100.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/5/200/smartPoints200.txt ../input/5/200/p200.txt ../output/oneSSA/5/200/solution200.txt ../input/MIP.properties

javac -cp ".;%CPLEX_JAR%" SSAHv2.java

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/5/50/smartPoints50.txt ../input/5/50/p50.txt ../output/multipleSSA/5/50/solution50.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/5/100/smartPoints100.txt ../input/5/100/p100.txt ../output/multipleSSA/5/100/solution100.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/5/200/smartPoints200.txt ../input/5/200/p200.txt ../output/multipleSSA/5/200/solution200.txt ../input/MIP.properties