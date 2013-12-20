package main;

import java.util.ArrayList;
import java.util.List;

import priors.FixedValuePrior;
import priors.NormalPrior;

import inference.*;
import models.*;
import abc.*;

/**
 * example inference class - simulates data from an SIR model then uses StepDownRejector to estimate the parameters,
 * contains a "main" method.
 * This class was created as an early test of the models, see LuckyGuess for the actuall main class.
 * @author Samantha Lycett
 * @created 6 Dec 2013
 * @version 6 Dec 2013
 *
 */
public class StepDownSIR {
	
	boolean verbose = true;
	
	ModelInferer inferer;
	
	
	public StepDownSIR() {
		
	}
	
	public void initialise() {
		List<ParameterWithPrior> 	modelParametersWithPriors 	= generateInitialModelParametersWithPriors();
		List<Statistic> 			targetStatistics 			= simulateData();
		Model 						modelType 					= new SIRModel();
		int 						numModelsPerBatch			= 100;
		
		StepDownRejector.setVerbose(verbose);
		inferer = new StepDownRejector(modelParametersWithPriors, targetStatistics, modelType, numModelsPerBatch);
		((StepDownRejector)inferer).setMaxIterations(10);
		((StepDownRejector)inferer).setInitialToleranceFraction(0.5);
		((StepDownRejector)inferer).setToleranceReductionFactor(0.8);
	}
	
	public void run() {
		inferer.run();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	/**
	 * use this to simulate data for testing purposes
	 */
	List<Statistic> simulateData() {
		
		if (verbose) {
			System.out.println("------------------------");
			System.out.println("Simulating Data and Gathering Target Statistics for Test");
		}
		
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
		List<Statistic> targetStatistics = new ArrayList<Statistic>();
		for (Statistic s : simStats) {
			ResultStatistic rs = (ResultStatistic)s;
			rs.setToleranceAsFraction(0.1);
			targetStatistics.add(rs);
		}
		
		if (verbose) {
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
		
		return targetStatistics;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	/**
	 * use this to generate initial model parameters and priors
	 * @return
	 */
	List<ParameterWithPrior> generateInitialModelParametersWithPriors() {
		
		List<ParameterWithPrior> modelParametersWithPrior = new ArrayList<ParameterWithPrior>();
		modelParametersWithPrior.add( new ParameterWithPrior("N", 1000, new FixedValuePrior(1000) ) );
		modelParametersWithPrior.add( new ParameterWithPrior("b", 2,    new NormalPrior(2.0, 1.0, 0.5, 2.5)) );
		modelParametersWithPrior.add( new ParameterWithPrior("y", 1,	new NormalPrior(1.0, 1.0, 0.5, 2.5)) );
		modelParametersWithPrior.add( new ParameterWithPrior("initialI", 1, new FixedValuePrior(1)));
		modelParametersWithPrior.add( new ParameterWithPrior("dt", 0.1, new FixedValuePrior(0.1)));
		modelParametersWithPrior.add( new ParameterWithPrior("maxReps", 1000, new FixedValuePrior(1000)));
		
		return modelParametersWithPrior;
		
	}
	

	///////////////////////////////////////////////////////////////

	public static void main(String[] args) {
	
		StepDownSIR example = new StepDownSIR();
		example.initialise();
		example.run();
		
	}
	
	
}
