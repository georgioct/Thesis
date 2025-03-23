@echo off

REM 
set PROJECT_DIRECTORY=C:\Users\Giorgos\Desktop\Thesis\src
set CPLEX_JAR=C:\Users\Giorgos\Desktop\Thesis\lib\cplex.jar
REM

cd %PROJECT_DIRECTORY%


javac -cp ".;%CPLEX_JAR%" SSAHv1.java

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/500/smartPoints500.txt ../input/20/500/p500_1.txt ../output/oneSSA/20/500/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/500/smartPoints500.txt ../input/20/500/p500_2.txt ../output/oneSSA/20/500/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/500/smartPoints500.txt ../input/20/500/p500_3.txt ../output/oneSSA/20/500/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/500/smartPoints500.txt ../input/20/500/p500_4.txt ../output/oneSSA/20/500/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/500/smartPoints500.txt ../input/20/500/p500_5.txt ../output/oneSSA/20/500/solution5.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/500/smartPoints500.txt ../input/20/500/p500_6.txt ../output/oneSSA/20/500/solution6.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/500/smartPoints500.txt ../input/20/500/p500_7.txt ../output/oneSSA/20/500/solution7.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/500/smartPoints500.txt ../input/20/500/p500_8.txt ../output/oneSSA/20/500/solution8.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/500/smartPoints500.txt ../input/20/500/p500_9.txt ../output/oneSSA/20/500/solution9.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/500/smartPoints500.txt ../input/20/500/p500_10.txt ../output/oneSSA/20/500/solution10.txt ../input/MIP.properties

javac -cp ".;%CPLEX_JAR%" SSAHv2.java

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/500/smartPoints500.txt ../input/20/500/p500_1.txt ../output/multipleSSA/20/500/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/500/smartPoints500.txt ../input/20/500/p500_2.txt ../output/multipleSSA/20/500/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/500/smartPoints500.txt ../input/20/500/p500_3.txt ../output/multipleSSA/20/500/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/500/smartPoints500.txt ../input/20/500/p500_4.txt ../output/multipleSSA/20/500/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/500/smartPoints500.txt ../input/20/500/p500_5.txt ../output/multipleSSA/20/500/solution5.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/500/smartPoints500.txt ../input/20/500/p500_6.txt ../output/multipleSSA/20/500/solution6.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/500/smartPoints500.txt ../input/20/500/p500_7.txt ../output/multipleSSA/20/500/solution7.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/500/smartPoints500.txt ../input/20/500/p500_8.txt ../output/multipleSSA/20/500/solution8.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/500/smartPoints500.txt ../input/20/500/p500_9.txt ../output/multipleSSA/20/500/solution9.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/500/smartPoints500.txt ../input/20/500/p500_10.txt ../output/multipleSSA/20/500/solution10.txt ../input/MIP.properties



pause