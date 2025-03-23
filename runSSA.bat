@echo off

REM 
set PROJECT_DIRECTORY=C:\Users\Giorgos\Desktop\Thesis\src
set CPLEX_JAR=C:\Users\Giorgos\Desktop\Thesis\lib\cplex.jar
REM

cd %PROJECT_DIRECTORY%


javac -cp ".;%CPLEX_JAR%" SSAHv1.java

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/400/smartPoints400.txt ../input/20/400/p400_1.txt ../output/oneSSA/20/400/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/400/smartPoints400.txt ../input/20/400/p400_2.txt ../output/oneSSA/20/400/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/400/smartPoints400.txt ../input/20/400/p400_3.txt ../output/oneSSA/20/400/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/400/smartPoints400.txt ../input/20/400/p400_4.txt ../output/oneSSA/20/400/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/400/smartPoints400.txt ../input/20/400/p400_5.txt ../output/oneSSA/20/400/solution5.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/400/smartPoints400.txt ../input/20/400/p400_6.txt ../output/oneSSA/20/400/solution6.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/400/smartPoints400.txt ../input/20/400/p400_7.txt ../output/oneSSA/20/400/solution7.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/400/smartPoints400.txt ../input/20/400/p400_8.txt ../output/oneSSA/20/400/solution8.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/400/smartPoints400.txt ../input/20/400/p400_9.txt ../output/oneSSA/20/400/solution9.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/20/400/smartPoints400.txt ../input/20/400/p400_10.txt ../output/oneSSA/20/400/solution10.txt ../input/MIP.properties

javac -cp ".;%CPLEX_JAR%" SSAHv2.java

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/400/smartPoints400.txt ../input/20/400/p400_1.txt ../output/multipleSSA/20/400/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/400/smartPoints400.txt ../input/20/400/p400_2.txt ../output/multipleSSA/20/400/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/400/smartPoints400.txt ../input/20/400/p400_3.txt ../output/multipleSSA/20/400/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/400/smartPoints400.txt ../input/20/400/p400_4.txt ../output/multipleSSA/20/400/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/400/smartPoints400.txt ../input/20/400/p400_5.txt ../output/multipleSSA/20/400/solution5.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/400/smartPoints400.txt ../input/20/400/p400_6.txt ../output/multipleSSA/20/400/solution6.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/400/smartPoints400.txt ../input/20/400/p400_7.txt ../output/multipleSSA/20/400/solution7.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/400/smartPoints400.txt ../input/20/400/p400_8.txt ../output/multipleSSA/20/400/solution8.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/400/smartPoints400.txt ../input/20/400/p400_9.txt ../output/multipleSSA/20/400/solution9.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/20/400/smartPoints400.txt ../input/20/400/p400_10.txt ../output/multipleSSA/20/400/solution10.txt ../input/MIP.properties



pause