package main;

import abc.*;
import math.Distributions;
import models.SIRModel;

import java.util.*;

import priors.*;

/**
 * GuessSIRModels uses rejection sampling to guess the parameters of an SIR model
 * but it does successive iterations, improving the priors each time
 * contains a "main"
 * This class was created as an early test of the models.  See LuckyGuess for the actual main class.
 * @author Samantha Lycett
 * @created 21 Oct 2013
 * @version 21 Oct 2013
 *
 */
public class GuessSIRModel {

	List<Statistic> 			targetStatistics;
	List<ParameterWithPrior> 	modelParametersWithPrior;
	Model						modelType					= new SIRModel();
	int							repsPerBatch				= 500;
	int							numBatches					= 20;
	
	// for stepped rejections
	int							numIts						= 3;
	double						reduceToleranceFactor		= 0.5;
	
	BatchOfModels				bom							= new BatchOfModels();
	
	public GuessSIRModel() {
		
	}
	
	/**
	 * use this to simulate data for testing purposes
	 */
	void simulateData() {
		
		System.out.println("------------------------");
		System.out.println("Simulating Data and Gathering Target Statistics for Test");
		SIRModel modelToMakeData 		= new SIRModel();
		List<Parameter> modelParameters = new ArrayList<Parameter>();
		modelParameters.add(new Parameter("N", 1000));
		modelParameters.add(new Parameter("b", 2));
		modelParameters.add(new Parameter("y", 1));
		modelParameters.add(new Parameter("initialI", 1));
		modelParameters.add(new Parameter("dt", 0.1));
		modelParameters.add(new Parameter("maxReps", 1000));
		modelToMakeData.setParameters(modelParameters);
		
		modelToMakeData.run();
		
		// get stats
		List<Statistic> simStats = modelToMakeData.getStatistics();
		
		// add 10% tolerances to make target statistics
		targetStatistics = new ArrayList<Statistic>();
		for (Statistic s : simStats) {
			ResultStatistic rs = (ResultStatistic)s;
			rs.setToleranceAsFraction(0.1);
			targetStatistics.add(rs);
		}
		
		System.out.println("Made an SIR Model with parameters:");
		for (Parameter p : modelParameters) {
			System.out.println(p);
		}
		
		System.out.println("Ran the SIR Model to generate data with target statistics:");
		for (Statistic s : targetStatistics) {
			System.out.println((ResultStatistic)s);
		}
		System.out.println("------------------------");
	}
	
	void initialise() {

		System.out.println("------------------------");
		System.out.println("Initialise Batch of Models");
		modelParametersWithPrior = new ArrayList<ParameterWithPrior>();
		modelParametersWithPrior.add( new ParameterWithPrior("N", 1000, new FixedValuePrior(1000) ) );
		modelParametersWithPrior.add( new ParameterWithPrior("b", 2,    new NormalPrior(2.0, 1.0, 0.5, 2.5)) );
		modelParametersWithPrior.add( new ParameterWithPrior("y", 1,	new NormalPrior(1.0, 1.0, 0.5, 2.5)) );
		modelParametersWithPrior.add( new ParameterWithPrior("initialI", 1, new FixedValuePrior(1)));
		modelParametersWithPrior.add( new ParameterWithPrior("dt", 0.1, new FixedValuePrior(0.1)));
		modelParametersWithPrior.add( new ParameterWithPrior("maxReps", 1000, new FixedValuePrior(1000)));
		
		bom.setModelType(modelType);
		bom.setModelParametersWithPriors(modelParametersWithPrior);
		bom.setTargetStats(targetStatistics);
		bom.setNreps(repsPerBatch);
		//bom.setup();
	}
	
	public void runRejection() {

		System.out.println("------------------------");
		System.out.println("Run Rejection Sampler with Batches of Models");
		for (int k = 1; k <= numBatches; k++) {
			System.out.println("Batch "+k);
			
			// choose parameters for this batch of models
			bom.setup();
			
			// run one batch of models
			bom.run();	
			
			// harvest good models
			bom.summariseGoodModels();
		}
	}
	
	public void runStepped() {
		System.out.println("------------------------");
	
		System.out.println("Run Stepped Rejection Sampler with Batches of Models");
		
		System.out.println("------------------------------------");
		System.out.println("-- Step 0 - First Run of Rejection Sampler --");
		runRejection();
		printSummaryOfResults();
		
		int iteration = 1;
		while (iteration <= numIts) {
		
			System.out.println("---------------------------");
			System.out.println("** Iteration "+iteration + "**");
			iteration++;
			
			System.out.println("---------------------------");
			System.out.println("-- Alter priors --");
			// generate new parameter priors
			modelParametersWithPrior = new ArrayList<ParameterWithPrior>();
			for (SummaryResultStatistic srs : bom.getGoodParameterEstimates() ) {
				modelParametersWithPrior.add( srs.generateNewParameterWithNormalPriorFromResults() );
			}
			bom.setModelParametersWithPriors(modelParametersWithPrior);
			bom.resetSummary();
		
			System.out.println("-------------------------------");
			System.out.println("-- Reduce Tolerance by Factor of "+reduceToleranceFactor+"--");
			ArrayList<Statistic> newTargetStatistics = new ArrayList<Statistic>();
			for (Statistic s : targetStatistics) {
				ResultStatistic rs = (ResultStatistic)s;
				rs.setToleranceAsFraction( (rs.getTolerance()/rs.getValue())*reduceToleranceFactor );
				newTargetStatistics.add(rs);
			}
			targetStatistics = newTargetStatistics;
			bom.setTargetStats(targetStatistics);
		
			System.out.println("---------------------------------------");
			System.out.println("-- Re-Run Rejection Sampler --");
			runRejection();
			printSummaryOfResults();
		
		}
		
	}

	public void printSummaryOfResults() {
			
		// get results
		System.out.println("------------------");
		System.out.println("* Summary of Results *");
		
		System.out.println("* Target Statistics:");
		for (Statistic target : targetStatistics) {
			System.out.println(target);
		}
		
		System.out.println("* Parameter priors:");
		for (ParameterWithPrior mpp : modelParametersWithPrior) {
			System.out.println(mpp);
		}
		
		System.out.println("* Actual result statistics:");
		System.out.println("Models examined:\t"+bom.getTotalModelsExamined());
		for (SummaryResultStatistic res : bom.getGoodResultStatistics()) {
			System.out.println(res);
		}
		
		System.out.println("* Actual parameter estimates:");
		for (SummaryResultStatistic res : bom.getGoodParameterEstimates()) {
			System.out.println(res);
		}
		System.out.println("------------------");
}
	
	///////////////////////////////////////////////////////////
	
	public static void rejectionTest() {
		
		// for testing only
		Distributions.initialiseWithSeed(54321);
				
		System.out.println("- Test Rejection Sampler ABC -");
		GuessSIRModel guess = new GuessSIRModel();
		guess.simulateData();
		guess.initialise();
		guess.runRejection();
		guess.printSummaryOfResults();
		
	}
	
	public static void steppedRejectionTest() {
		
		// for testing only
		Distributions.initialiseWithSeed(54321);
						
		System.out.println("- Test Stepped Rejection Sampler ABC -");
		GuessSIRModel guess = new GuessSIRModel();
		guess.simulateData();
		guess.initialise();
		guess.runStepped();
		System.out.println("** FINAL RESULTS **");
		guess.printSummaryOfResults();
				
	}
	
	public static void main(String[] args) {
		
		System.out.println("** GuessSIRModel **");
		
		//rejectionTest();
		steppedRejectionTest();
		
		System.out.println("** END **");
		
	}
	
}
