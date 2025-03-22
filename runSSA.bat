@echo off

REM 
set PROJECT_DIRECTORY=C:\Users\Giorgos\Desktop\Thesis\src
set CPLEX_JAR=C:\Users\Giorgos\Desktop\Thesis\lib\cplex.jar
REM

cd %PROJECT_DIRECTORY%


javac -cp ".;%CPLEX_JAR%" SSAHv1.java

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_1.txt ../output/oneSSA/10/200/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_2.txt ../output/oneSSA/10/200/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_3.txt ../output/oneSSA/10/200/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_4.txt ../output/oneSSA/10/200/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/200/smartPoints200.txt ../input/10/200/p200_5.txt ../output/oneSSA/10/200/solution5.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/400/smartPoints400.txt ../input/10/400/p400_1.txt ../output/oneSSA/10/400/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/400/smartPoints400.txt ../input/10/400/p400_2.txt ../output/oneSSA/10/400/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/400/smartPoints400.txt ../input/10/400/p400_3.txt ../output/oneSSA/10/400/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/400/smartPoints400.txt ../input/10/400/p400_4.txt ../output/oneSSA/10/400/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/400/smartPoints400.txt ../input/10/400/p400_5.txt ../output/oneSSA/10/400/solution5.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/500/smartPoints500.txt ../input/10/500/p500_1.txt ../output/oneSSA/10/500/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/500/smartPoints500.txt ../input/10/500/p500_2.txt ../output/oneSSA/10/500/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/500/smartPoints500.txt ../input/10/500/p500_3.txt ../output/oneSSA/10/500/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/500/smartPoints500.txt ../input/10/500/p500_4.txt ../output/oneSSA/10/500/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv1 ../input/10/500/smartPoints500.txt ../input/10/500/p500_5.txt ../output/oneSSA/10/500/solution5.txt ../input/MIP.properties


javac -cp ".;%CPLEX_JAR%" SSAHv2.java

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_1.txt ../output/multipleSSA/10/200/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_2.txt ../output/multipleSSA/10/200/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_3.txt ../output/multipleSSA/10/200/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_4.txt ../output/multipleSSA/10/200/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/200/smartPoints200.txt ../input/10/200/p200_5.txt ../output/multipleSSA/10/200/solution5.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/400/smartPoints400.txt ../input/10/400/p400_1.txt ../output/multipleSSA/10/400/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/400/smartPoints400.txt ../input/10/400/p400_2.txt ../output/multipleSSA/10/400/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/400/smartPoints400.txt ../input/10/400/p400_3.txt ../output/multipleSSA/10/400/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/400/smartPoints400.txt ../input/10/400/p400_4.txt ../output/multipleSSA/10/400/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/400/smartPoints400.txt ../input/10/400/p400_5.txt ../output/multipleSSA/10/400/solution5.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/500/smartPoints500.txt ../input/10/500/p500_1.txt ../output/multipleSSA/10/500/solution1.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/500/smartPoints500.txt ../input/10/500/p500_2.txt ../output/multipleSSA/10/500/solution2.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/500/smartPoints500.txt ../input/10/500/p500_3.txt ../output/multipleSSA/10/500/solution3.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/500/smartPoints500.txt ../input/10/500/p500_4.txt ../output/multipleSSA/10/500/solution4.txt ../input/MIP.properties

java -cp ".;%CPLEX_JAR%" SSAHv2 ../input/10/500/smartPoints500.txt ../input/10/500/p500_5.txt ../output/multipleSSA/10/500/solution5.txt ../input/MIP.properties


pause