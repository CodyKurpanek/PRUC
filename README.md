

# Applying PRUC to REGAL Redistricting
This is the fork of the [PRUC](https://github.com/Yongyi-Liu/PRUC): P-Regions with User-Defined Constraints repository where we apply this method to the school redistricting problem. PRUC is a general method for taking a set of areas and grouping it into exactly P distinct regions, or spatially contiguous groups of areas,  such that each region satisfies some user defined threshold, and the intra-region similarity according defined similarity features is optimized. The similarity features and user defined threshold pertain to aggregate values of attributes within a region as a whole. 

We apply this method to the school redistricting problem defined by [REGAL](https://github.com/subhodipbiswas/REGAL): A Regionalization framework for school boundaries, and compare PRUC's results to those of the method proposed in REGAL. REGAL seeks to find districts such that each district must have exactly one school, and the districts are optimized in terms of the region's shape and the ratio between the region's student population and the school's capacity. While PRUC primarily seeks to optimize intra-region similarity, REGAL's problem requires optimization of features internal to each region. Nevertheless, PRUC's method applies nicely to the Redistricting problem.

# Our Paper
Please read our unpublished [paper](https://drive.google.com/file/d/1HCR8Bi3cXAn8BumwmGwgcr67VkG7vrgA/view) for more on our process and findings!

Cody Kurpanek, Karan Bhogal, Taneesha Sharma, Tyler Pastor 



# Setup
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

#### Environment:
The code is written in Java 14. The original developing platform is IntelliJ IDEA. The maven version is 3.6.3. pom.xml stores all the denpencies for this project.


#### Explanation:
The util package stores the common Class that are used among all the methods.
The GSLO package stores all the Class related to Global Search and Local Optimization.
The baseline package stores our baseline competitors, it is further divided into greedy, SKATER and SKATERCON.

