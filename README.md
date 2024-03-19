### Pruc Applied to Regal.
This is the fork of the PRUC: P-Regions with User-Defined Constraints repository. We use apply this model to the redistricting problem. Please look at the PRUC description further below for an explanation of the PRUC method.

For a description of what we did, please check our report.

#### How to run the code and read the output:
Change the dataset in src/main/java/test/TestGeneral.java to either 
redistrictingES, redistrictingMS, or redistrictingHS.

Click play button on IntelliJ or run the following 2 commands:

mvn compile

mvn exec:java -Dexec.mainClass=test.TestGeneral


The output will first show preprocessing information of how many schools are matched with a unique SPA and will show the seeded regions.

Next, we output data that can be plotted in order to visualize how our algorithm does.

The first output displayed which areas were marked as the seeded areas. The seed identification step was quite simple to implement because we were already given what areas contain a school. This means that we were able to meet the user-defined constraint (a region must contain one school and one school only) in the first process of the global search phase. At this point, we can grow these seeded areas (regions) in the next process. Once the region growth process finishes with all regions being complete and no enclaved assignments, we output all areas respective region ID and SPA ID. Each of these output was used to visualize the partitions of the district. We created a python script, using the GeoPandas and Matplotlib library to produce several maps that depict these partitions. 

Please check the Visualize folder to see how to use these values to visualize the districts.



#### PRUC:
This is the source code and dataset for the paper PRUC: P-Regions with User-Defined Constraint.

#### Environment:
The code is written in Java 14. The original developing platform is IntelliJ IDEA. The maven version is 3.6.3. pom.xml stores all the denpencies for this project.

#### Quick Start:
The following provides a quick way to run the code and reproduce the experimental results in command line. First switch to the project directory run **-mvn compile** to compile the java source classes, which gives the following output.  
<img src = "https://github.com/Yongyi-Liu/PRUC/blob/master/cmdline/step1.png" width = "400">

Then, type **-mvn exec:java -Dexec.mainClass=test.TestGeneral** to run the main class in the project, and the experiments will be run sequentially. The following is an example.

<img src = "https://github.com/Yongyi-Liu/PRUC/blob/master/cmdline/step2.png" width = "900">

#### Explanation:
The util package stores the common Class that are used among all the methods.
The GSLO package stores all the Class related to Global Search and Local Optimization.
The baseline package stores our baseline competitors, it is further divided into greedy, SKATER and SKATERCON.

#### Dataset:
The datasets used in experiment come from two sources,  [TIGER/Line Shapefile](https://catalog.data.gov/dataset/tiger-line-shapefile-2016-series-information-for-the-current-census-tract-state-based-shapefile "TIGER/Line Shapefile") dataset and [2000 Health, Income and Diversity](https://geodacenter.github.io/data-and-lab/co_income_diversity_variables/ "2000 Health, Income and Diversity") dataset. The original datasets are slightly modified by removing the island areas. The [diversity](https://github.com/Yongyi-Liu/PRUC/tree/master/DataFile/30K "diversity") folder under DataFile folder refers to the 2000 Health, Income and Diversity dataset. All other datasets are from TIGER/Line Shapefile dataset. Different states in TIGER/Line Shapefile dataset are merged to form larger datasets.

#### Contact:
If you have any question with this project, please feel free to reach me by yliu786 [at] ucr.edu



