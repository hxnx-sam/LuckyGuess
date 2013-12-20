package inference;

import java.util.ArrayList;
import java.util.List;
import abc.*;

/**
 * class to implement step wise rejection abc, with linearly reducing tolerance each time
 * @author Samantha Lycett
 * @created 6 Dec 2013
 * @version 6 Dec 2013
 * @version 11 Dec 2013
 */
public class StepDownRejector implements ModelInferer {
	
	// Class variables
	static boolean 				verbose 	= true;
		
	// Class methods
	public static void setVerbose(boolean v) {
		verbose = v;
	}
		
	///////////////////////////////////////////////////////////////////////////
	// Instance variables & methods
		
	BatchOfModels 				bom 		= new BatchOfModels();
	
	int 						maxIts 					 = 10;
	double						initialToleranceFraction = 0.5;		// 50%
	double 						tolfactor 				 = 0.8;		// i.e. reduce tolerance fraction by 20% of current each iteration
	
		
	//////////////////////////////////////////
	// CONSTRUCTORS
	
	
	public StepDownRejector() {
		
	}
	
	
	public StepDownRejector(List<ParameterWithPrior> priors, List<Statistic> targetStats, Model modelType, int numModelsPerStep) {
		setModelParametersWithPriors(priors);
		setTargetStats(targetStats);
		setModelType(modelType);
		setNumModels(numModelsPerStep);
	}
	

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
	/**
	 * set number of models per iteration batch
	 */
	public void setNumModels(int nreps) {
		bom.setNreps(nreps);
	}

	public void setNumModelsPerIteration(int nreps) {
		setNumModels(nreps);
	}
	
	public void setMaxIterations(int maxIts) {
		this.maxIts = maxIts;
	}
	
	public void setInitialToleranceFraction(double tolfract) {
		this.initialToleranceFraction = tolfract;
		setToleranceAsFraction(initialToleranceFraction);
	}
	
	public void setToleranceReductionFactor(double tolfactor) {
		this.tolfactor = tolfactor;
	}
	
	///////////////////////////////////////////////////////////////////////
	
	@Override
	public void run() {
		
		/////////////////////////////////////////////
		// iterations 0 to max-1
		
		setToleranceAsFraction(initialToleranceFraction);
		bom.resetSummary();
		
		int numIts = 0;
		while (numIts < maxIts) {
			
			if (numIts > 0) {
				// update parameters based on previous results
				bom.updateModelParametersWithGoodResults();
				
				// reduce tolerance
				reduceToleranceByFactor(tolfactor);
			}
			
			// choose parameters for this batch of models
			bom.setup();
			
			// reset results summary statistics for this iteration
			bom.resetSummary();
			
			// run one batch of models
			bom.run();
			
			// harvest good models
			bom.summariseGoodModels();
			
			if (verbose) {
				System.out.println("Iteration "+numIts);
				printSummaryOfResults();
				System.out.println();
				
				/*
				System.out.println("All Result Statistics");
				for (SummaryResultStatistic stat : bom.summariseAllModelResultStatistics()) {
					System.out.println(stat.toString());
				}
				
				System.out.println("All Parameter Estimates");
				for (SummaryResultStatistic stat : bom.summariseAllModelParameterEstimates()) {
					System.out.println(stat.toString());
				}
				*/
			}
			
			numIts++;
		}
		
	}

	@Override
	public List<SummaryResultStatistic> getGoodParameterEstimates() {
		return bom.getGoodParameterEstimates();
	}

	@Override
	public List<SummaryResultStatistic> getGoodResultStatistics() {
		return bom.getGoodResultStatistics();
	}

	//////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * sets tolerances for all target statistics to be the input fraction (tol)
	 * e.g. set tolerances for all to be 5% of the target value = 0.05
	 * @param tol
	 */
	void setToleranceAsFraction(double tol) {
				
		List<Statistic> targetStatistics = new ArrayList<Statistic>();
		for (Statistic s : bom.getTargetStats() ) {
			ResultStatistic rs = (ResultStatistic)s;
			rs.setToleranceAsFraction(tol);
			targetStatistics.add(rs);
		}
		bom.setTargetStats(targetStatistics);
		
	}
	
	/**
	 * reduces tolerances for all target statistics by the input factor
	 * e.g. to reduce all tolerances by 10% of current use 0.9
	 * e.g. to increase all tolerances by factor of 2 (double), use 2.
	 * @param factor
	 */
	private void reduceToleranceByFactor(double factor) {
		for (Statistic stat : bom.getTargetStats()) {
			Double currentTol 	= ((ResultStatistic)stat).getTolerance();
			Double newTol		= factor*currentTol;
			((ResultStatistic)stat).setTolerance(newTol);
		}
	}
	
	/*
	 * updates model parameters (mean and stdev if possible) based on the results of the previous round
	 */
	/*
	private void updatePriorsBasedOnPreviousResults() {
		bom.updateModelParametersWithGoodResults();
		bom.resetSummary();
	}
	*/
	
	void printSummaryOfResults() {
		
		// get results
		System.out.println("------------------");
		System.out.println("* Summary of Results *");
		
		System.out.println("* Target Statistics:");
		for (Statistic target : bom.getTargetStats()) {
			System.out.println(target);
		}
		
		System.out.println("* Parameter priors:");
		for (ParameterWithPrior mpp : bom.getModelParametersWithPriors()) {
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
	
	public String toString() {
		String eol 		 = "\n";
		StringBuffer txt = new StringBuffer();
		
		txt.append("InferenceType="+this.getClass().getSimpleName()+eol);
		txt.append("MaxIterations="+maxIts+eol);
		txt.append("InitialTolerance="+initialToleranceFraction+eol);
		txt.append("ToleranceReductionFactor="+tolfactor+eol);
		txt.append(bom.toString()+eol);
		
		return txt.toString();
	}
	
}
