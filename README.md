# Sparse Multiplication Using Spark

Develop a sparse multiplication application using spark mllib and compare the performance of this application after pre-partitioning the matrices

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Requirements

* Download IntelliJ IDEA for scala application development from https://www.jetbrains.com/idea/download/#section=linux
* Install the Scala Plugin from the IntelliJ IDEA.
* Python

## Setup

* Pull the project and cd to the project directory.
* Run the python program matrixgenerator.py to generate the input matrices. Install the required libraries if an error is thrown. This will generate 3 files - Matrix1.txt, Matrix2.txt and Result.txt. First 2 will be used in the scala program and the result.txt is present to verify the results if required.

## Progress

Completed -> Part 1 and Part 2 (Partial). Have to set up and configure the google cloud VM instances for Part 2. 
Currently the matrix that will be generated will be a 5 X 5 matrix with 0.15 sparse density. This can be changed inside the python program (matrixgenerator.py) if required. I will change it later to set it as user input values.
