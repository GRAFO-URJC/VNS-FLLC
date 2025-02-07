# [__NAME__](https://doi.org/XXXXX)

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
Paper abstract or summary of the article submitted.

## Authors

### Person1
<table>
<tr>
<td>

**Institution:** [Department Name, University Name](https://institution1.edu)  
**Email:** [person1@institution1.edu](mailto:person1@institution1.edu)  
**ORCID:** [0000-0000-0000-0001](https://orcid.org/0000-0000-0000-0001)  
**Role:** Lead researcher, methodology development

</td>
</tr>
</table>

### Person2
<table>
<tr>
<td>

**Institution:** [Department Name, University Name](https://institution2.edu)  
**Email:** [person2@institution2.edu](mailto:person2@institution2.edu)  
**ORCID:** [0000-0000-0000-0002](https://orcid.org/0000-0000-0000-0002)  
**Role:** Data analysis, implementation

</td>
</tr>
</table>

## Datasets
Instances are categorized in different datasets inside the 'instances' folder. All instances are from the [UCI Machine Learning Repository](https://archive.ics.uci.edu/ml/index.php) or reference to paper where they are from, etc.

### Instance format
(Explain instance format so other users may easily use them even if not using your code.)

## Development

### Requirements and Dependencies
- Java 11 or higher
- Maven 3.6 or higher

### Compiling
You can easily compile and build an executable artifact of this project using:
```text
mvn clean package
```

### Executing
There is a executable JAR inside the code folder.

Example 1: execute default experiment with the default set of instances
```text
java -jar target/ACO.jar 
```

Example 2: execute the default experiment using a different set of instances, located inside the `newinstances` folder.
```text
java -jar target/ACO.jar --path=newinstances
```

## Cite
Consider citing our paper if used in your own work:
(Fill with the references to your own published work)

### DOI
https://doi.org/XXXXXXX

### Zenodo
https://doi.org/XXXXXXX

### BibTeX
```bibtex
@article{
...
}
```

## Powered by MORK (Metaheuristic Optimization framewoRK)
| ![Mork logo](https://user-images.githubusercontent.com/55482385/233611563-4f5c91f2-af36-4437-a4b5-572b6655487a.svg) | Mork is a Java framework for easily solving hard optimization problems. You can [create a project](https://generator.mork-optimization.com/) and try the framework in under one minute. See the [documentation](https://docs.mork-optimization.com/en/latest/) or the [source code](https://github.com/mork-optimization/mork). |
|--|--|
