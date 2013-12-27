LuckyGuess
==========

LuckyGuess implements ABC schemes for SIR type models in order to investigate the efficiency of different schemes (java)

This repository contains the LuckyGuess code as an Eclipse java project.
Dependencies - you will need to add JUnit test to the project set up.

Directories:
* src - the source code (.java)
* bin - the built java classes (.class)
* exampleConfigs - xml input files for LuckyGuess and LuckySimulation
* test - directory where the results of LuckyGuess and LuckySimulation can be written for testing purposes

To run:
27 Dec 2013 - This project is under development, so at the moment you will need to load it into Eclipse and then run a class from package "main".
* LuckyGuess - reads parameters from the input xml and performs the ABC inference
* LuckySimulation - reads parameters from the input xml and runs a bunch of simulations only.
