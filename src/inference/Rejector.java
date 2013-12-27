package inference;

import java.util.List;

import abc.BatchOfModels;
import abc.Model;
import abc.ParameterWithPrior;
import abc.ResultStatistic;
import abc.Statistic;
import abc.SummaryResultStatistic;

/**
 * class to run one round of ABC and accept or reject models based on target statistics
 * @author sam
 * @version 11 Dec 2013
 */
public class Rejector implements ModelInferer {
	
	// Class variables
	static boolean 				verbose 	= true;
	
	// Class methods
	public static void setVerbose(boolean v) {
		verbose = v;
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Instance variables & methods
	
	
	BatchOfModels 				bom 		= new BatchOfModels();
	private boolean 			initialise 	= true;
	
	// for writing output files
	String 						path;
	String 						rootname;
	
	//////////////////////////////////////////
	// CONSTRUCTORS
	
	public Rejector() {
		
	}
	
	public Rejector(List<ParameterWithPrior> priors, List<Statistic> targetStatistic, Model modelType, int nreps) {
		setModelParametersWithPriors(priors);
		setTargetStats(targetStatistic);
		setModelType(modelType);
		setNumModels(nreps);
		
		////////////////////////////////////
		// initialise BatchOfModels (bom)
		if (verbose) {
			System.out.println("Initialise batch of models");
		}
		bom.setup();
		bom.resetSummary();			// do not accumulate statistics over many runs
		
		initialise = false;
		
	}
	
	//////////////////////////////////////////
	// methods

	@Override
	public void setTargetStats(List<Statistic> targetStatistic) {
		bom.setTargetStats(targetStatistic);
	}

	@Override
	public void setModelParametersWithPriors(List<ParameterWithPrior> priors) {
		bom.setModelParametersWithPriors(priors);
	}

	@Override
	public void setModelType(Model modelType) {
		bom.setModelType(modelType);
	}
	
	@Override
	public void setNumModels(int nreps) {
		bom.setNreps(nreps);
	}
	
	public void setToleranceFraction(double tol) {
		for (Statistic stat : bom.getTargetStats() ) {
			((ResultStatistic)stat).setToleranceAsFraction(tol);
		}
		
	}

	@Override
	public void setOutputPath(String path) {
		this.path = path;
	}
	
	@Override
	public void setOutputRootname(String rootname) {
		this.rootname = rootname;
	}
	
	//////////////////////////////////////////////////////////////////

	@Override
	public void run() {
		
		if (initialise) {
			////////////////////////////////////
			// initialise BatchOfModels (bom)
			// if Rejector from constructor with parameters then this has already been done in constructor
			if (verbose) {
				System.out.println("Initialise batch of models");
			}
			bom.setup();
		}
		bom.resetSummary();			// do not accumulate statistics over many runs
		
		///////////////////////////////////
		// run all the models
		if (verbose) {
			System.out.println("Run batch of models");
		}
		bom.run();
		

		////////////////////////////////////////////////////////////
		// summarise the models - compare them to target statistics
		if (verbose) {
			System.out.println("Compare results from models to target statistics");
		}
		bom.summariseGoodModels();
		
	}
	
	@Override
	public List<SummaryResultStatistic> getGoodParameterEstimates() {
		return bom.getGoodParameterEstimates();
	}
	
	@Override
	public List<SummaryResultStatistic> getGoodResultStatistics() {
		return bom.getGoodResultStatistics();
	}
	


}
