# [An efficient Variable Neighborhood Search approach for the Facility Location problem with Limited Choice rule](https://doi.org/10.1111/itor.70069)

## Table of Contents
- [Abstract](#abstract)
- [Authors](#authors)
- [Datasets](#datasets)
  - [Instance Format](#instance-format)
- [Development](#development)
  - [Requirements and Dependencies](#requirements-and-dependencies)
  - [Compiling](#compiling)
  - [Executing](#executing)
- [Citation](#cite)
  - [DOI](#doi)
  - [Zenodo](#zenodo)
  - [BibTeX](#bibtex)
- [Framework](#powered-by-mork-metaheuristic-optimization-framework)

## Abstract
Expansion of companies require to decide the most appropriate locations for their facilities. This decision problem, known as facility location problem, has been studied from different perspectives. The Facility Location problem with Limited Choice rule considers both the cost of opening facilities and the benefit of attracting customers under a deterministic utility approach. In this work, we propose a metaheuristic approach based on Variable Neighborhood Search to tackle this problem. Our proposal is able to obtain all the best results in the studied instances compared with state-of-the-art algorithms. In addition, we propose an efficient local search able to reduce the execution time, which enables the study of large size instances.

## Authors

### Enrique García-Galán
<table>
<tr>
<td>

**Institution:** [Universidad Rey Juan Carlos](https://urjc.es)  
**Email:** [enrique.garciag@urjc.es](mailto:enrique.garciag@urjc.es)  
**ORCID:** [0000-0001-5560-7270](https://orcid.org/0000-0001-5560-7270)  

</td>
</tr>
</table>

### Alberto Herrán
<table>
<tr>
<td>

**Institution:** [Universidad Rey Juan Carlos](https://urjc.es)  
**Email:** [alberto.herran@urjc.es](mailto:alberto.herran@urjc.es)  
**ORCID:** [0000-0003-0348-0313](https://orcid.org/0000-0003-0348-0313)  

</td>
</tr>
</table>

### Jose Manuel Colmenar
<table>
<tr>
<td>

**Institution:** [Universidad Rey Juan Carlos](https://urjc.es)  
**Email:** [josemanuel.colmenar@urjc.es](mailto:josemanuel.colmenar@urjc.es)  
**ORCID:** [0000-0001-7490-9450](https://orcid.org/0000-0001-7490-9450)  

</td>
</tr>
</table>

## Datasets
Instances are categorized in different datasets inside the 'instances' folder.

### Instance format
There are two types of instances with a similar format. The OR dataset instances do not contain all the same values as the other instances, therefore take note of the differences.

The complete format is:
```text
I: [i]
J: [j]
COST: [c]
COSTS:
[c1]
[c2]
...
[cj]
U0: [ui0]
SEED: [s]
GAMMA: [g]
GAMMAS:
[c1]
[c2]
...
[cj]
CUSTOMERS:
[x1] [y1]
[x2] [y2]
...
[xi] [yi]
FACILITIES:
[x1] [y1]
[x2] [y2]
...
[xj] [yj]
BUYING POWER:
[b1]
[b2]
...
[bi]
PI:
[pi11] [p12] ... [pi1j]
[pi21] [p22] ... [pi2j]
...
[pii1] [pii2] ... [piij]
```
The parameters are:
- I: Number of CUSTOMERS
- J: Number of FACILITIES
- COST: Type of cost. At the moment there is a fixed cost for each FACILITY
- COSTS: Cost of opening each FACILITY
- U0: Maximum capacity of a FACILITY
- SEED: Random seed used for the generation
- GAMMA: Type of gamma. Homogeneous (1,2,3,4,5,7,10,20) or non-homogeneous ("nh")
- GAMMAS: maximum number of facilities in the consideration set for each CUSTOMER
- CUSTOMERS: Coordinates of the CUSTOMERS
- FACILITIES: Coordinates of the FACILITIES
- BUYING POWER: Buying power of the CUSTOMERS
- PI: Calculated values for pi

The OR dataset format is similar without the following fields:
- COST
- FACILITIES
- CUSTOMERS
- SEED


## Development

### Requirements and Dependencies
- Java 21 or higher
- Maven 3.9 or higher

### Compiling
You can compile and build an executable artifact of this project using:

```shell
mvn clean package
```

### Executing
There is an executable JAR inside the code folder.

```shell
java -jar target/FLLC-1.00.jar
```

Or select a specific dataset to run the algorithm on:
```shell
java -jar target/FLLC-1.00.jar --instances.path.default=instances/R1
```

For OR instances the configuration should be changed to 2000 iterations.
```shell
java -jar target/FLLC-1.00.jar --instances.path.default=instances/ORlib --solver.repetitions=2000
```

The precompiled configuration will run t=50, 500 iterations, by default and all the instances.

#### How to configure the code

In case you would like to configure the parameters, you can compile it yourself, modify the parameters when you call the jar or execute the jar with a custom application.yml file.

If you want to include MsILS in your jar, you should go to `ConstructiveExperiment.java` and uncomment the line `this.MsILS(algorithms);`. In this case you will have to compile the code again.

If you want to change the number of iterations, you can do it in the `application.yml` file or give it as a parameter. The default value is 500, and you can change it to 2000 for OR instances.

### Execution results 

Given we use [Mork](https://github.com/mork-optimization/mork) all the executions are reproducibility. Because this algorithm is a multistart, you should take into account that to get the same results seen on the paper, you have to execute the jar with t=50 or t=200 and the group the results in the same order they are executed. For example, if you execute the jar with t=50, you will get 500 results, then you should group them in 10 groups of 50 results each. The first group will be the first 50 results, the second group will be the next 50 results and so on. For t=200 the same process should be done but with 10 groups of 200 results each.

## Cite

Consider citing our paper if used in your own work:
(Fill with the references to your own published work)

### DOI
https://doi.org/10.1111/itor.70069 

### Zenodo
https://doi.org/10.5281/zenodo.15480131

### BibTeX
```bibtex
@article{https://doi.org/10.1111/itor.70069,
author = {García-Galán, Enrique and Herrán, Alberto and Colmenar, J. Manuel},
title = {An efficient variable neighborhood search approach for the facility location problem with the limited choice rule},
journal = {International Transactions in Operational Research},
volume = {n/a},
number = {n/a},
pages = {},
keywords = {variable neighborhood search, facility location problem, limited choice rule},
doi = {https://doi.org/10.1111/itor.70069},
url = {https://onlinelibrary.wiley.com/doi/abs/10.1111/itor.70069},
eprint = {https://onlinelibrary.wiley.com/doi/pdf/10.1111/itor.70069},
abstract = {Abstract One of the most common problems in the expansion of a company consists of deciding the most appropriate locations for their facilities. This decision problem, known as the facility location problem, has been studied from different perspectives, considering a number of different constraints. Among these different versions of the problem, the facility location problem with the limited choice rule considers both the cost of opening facilities and the benefit of attracting customers under a deterministic utility approach. In this work, we propose a metaheuristic approach based on variable neighborhood search to tackle this problem. Our proposal is able to obtain the best results in 296 of the 309 instances studied, compared to state-of-the-art algorithms. In addition, we propose an efficient local search that obtains a 91.5\% average reduction in execution time compared to the version with the straightforward implementation. Therefore, this efficient proposal can be applied to larger instances that cannot be solved with previous approaches.}
}

```

## Powered by MORK (Metaheuristic Optimization framewoRK)
| ![Mork logo](https://user-images.githubusercontent.com/55482385/233611563-4f5c91f2-af36-4437-a4b5-572b6655487a.svg) | Mork is a Java framework for easily solving hard optimization problems. You can [create a project](https://generator.mork-optimization.com/) and try the framework in under one minute. See the [documentation](https://docs.mork-optimization.com/en/latest/) or the [source code](https://github.com/mork-optimization/mork). |
|--|--|
