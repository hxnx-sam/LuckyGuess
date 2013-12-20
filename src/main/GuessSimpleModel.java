package main;

import inference.*;

import java.util.*;

import priors.UniformPrior;
import models.SimpleModel;
import abc.*;

/**
 * GuessSimpleModel uses rejection sampling ABC to guess a 'midpoint' value
 * contains a "main"
 * This class was created as an early test of the models.  See LuckyGuess for the actual main class.
 * @author Samantha Lycett
 * @created 20 Oct 2013
 * @version 21 Oct 2013
 * @version 6 Dec 2013 		- use Rejector & ModelInferer interface
 */
public class GuessSimpleModel {

	Statistic targetStatistic = new ResultStatistic("midpoint", 10.0, 1.0);
	ParameterWithPrior mpp	  = new ParameterWithPrior("midpoint", 0, new UniformPrior(0,20));
	Model 	  modelType		  = new SimpleModel();
	int		  nreps			  = 1000;
	
	//BatchOfModels bom;
	ModelInferer inferer;
	
	public GuessSimpleModel() {
		
		Rejector.setVerbose(true);
		
		List<ParameterWithPrior> priors = new ArrayList<ParameterWithPrior>();
		priors.add(mpp);
		
		List<Statistic> targetStats = new ArrayList<Statistic>();
		targetStats.add(targetStatistic);
		
		inferer = new Rejector(priors, targetStats, modelType, nreps);
	}
	
	public void run() {
		
		inferer.run();
		
		/*
		////////////////////////////
		// initialise 		
		System.out.println("Initialise batch of models");
		
		bom = new BatchOfModels();
		
		List<Statistic> targetStats = new ArrayList<Statistic>();
		targetStats.add(targetStatistic);
		bom.setTargetStats(targetStats);
		
		mpp.setPrior(new UniformPrior(0,20) );
		List<ParameterWithPrior> modelParameters = new ArrayList<ParameterWithPrior>();
		modelParameters.add(mpp);
		bom.setModelParametersWithPriors(modelParameters);
		
		bom.setModelType(modelType);
		
		bom.setNreps(nreps);
		
		bom.setup();
	
		System.out.println("Run models");
		// run
		bom.run();
		*/
		
	}
	
	public void summaryOfResults() {
				
		// get results
		System.out.println("------------------");
		System.out.println("Summary of Results");
		System.out.println("Target value\t"+(ResultStatistic)targetStatistic);
		System.out.println("Parameter prior\t"+mpp);
		System.out.println("Number of models\t"+nreps);
		System.out.println("Actual results statistics:");
		for (SummaryResultStatistic res : inferer.getGoodResultStatistics()) {
			System.out.println(res);
		}
		System.out.println("Actual parameter estimates:");
		for (SummaryResultStatistic res : inferer.getGoodParameterEstimates()) {
			System.out.println(res);
		}
		System.out.println("------------------");
	}

	//////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) {
		System.out.println("** GuessSimpleModel **");
		System.out.println("- this is a rejection sampler ABC -");
		GuessSimpleModel guess = new GuessSimpleModel();
		guess.run();
		guess.summaryOfResults();
		
		System.out.println("** END **");
	}
	
}
