package abc;

import java.util.ArrayList;
import java.util.List;

/**
 * class to store and run a set of models; usage setup(), run(), summariseGoodModels()
 * @author Samantha Lycett
 * @created 19 Oct 2013
 * @version 20 Oct 2013
 * @version 12 Dec 2013
 * @version 15 Dec 2013
 *
 */
public class BatchOfModels {

	List<Statistic> 				targetStats;
	List<ParameterWithPrior> 		modelParametersWithPriors;
	
	int								nreps;
	Model							modelType;
	Model[] 						models;
	
	int								totalModels;
	List<Model>						goodModels;
	List<SummaryResultStatistic>	goodResultStatistics;
	List<SummaryResultStatistic>	goodParameterEstimates;
	
	public BatchOfModels() {
		resetSummary();
	}
	
	////////////////////////////////////////////////////////////////////

	public List<Statistic> getTargetStats() {
		return targetStats;
	}

	public void setTargetStats(List<Statistic> targetStats) {
		this.targetStats = targetStats;
	}

	public List<ParameterWithPrior> getModelParametersWithPriors() {
		return modelParametersWithPriors;
	}

	public void setModelParametersWithPriors(List<ParameterWithPrior> modelParameters) {
		this.modelParametersWithPriors = modelParameters;
	}

	public void setNreps(int nreps) {
		this.nreps = nreps;
	}
	
	public void setModelType(Model modelType) {
		this.modelType = modelType;
	}
	
	public List<SummaryResultStatistic> getGoodResultStatistics() {
		return goodResultStatistics;
	}

	public List<SummaryResultStatistic> getGoodParameterEstimates() {
		return goodParameterEstimates;
	}
	
	public Model[] getModels() {
		return models;
	}
	
	/**
	 * returns the summary (mean, dev, etc) for the named input parameter p
	 * @param p
	 * @return
	 */
	public SummaryResultStatistic getGoodParameterEstimate(Parameter p) {
		SummaryResultStatistic dummy 	= new SummaryResultStatistic();
		dummy.setName(p.getName());
		
		int pos 						= goodParameterEstimates.indexOf(dummy);
		return ( goodParameterEstimates.get(pos) );
	}
	
	/**
	 * updates priors for model parameters based on the mean and stdev of the previous results
	 */
	public void updateModelParametersWithGoodResults() {
		
		for (ParameterWithPrior p : modelParametersWithPriors) {
		
			SummaryResultStatistic dummy 	= new SummaryResultStatistic();
			dummy.setName(p.getName());
			
			//System.out.println(dummy.getName());
			
			int pos = goodParameterEstimates.indexOf(dummy);
			if (pos >= 0) {
				SummaryResultStatistic goodEst  = goodParameterEstimates.get(pos);
		
				double newMean = goodEst.getMean();
				double newDev  = goodEst.getDev();
			
				p.updatePriorMean(newMean);
				p.updatePriorStdev(newDev);
			} else {
				System.out.println("BatchOfModels.updateModelParametersWithGoodResults - warning could not update "+p.getName());
			}
			
		}
	}
	
	public int getTotalModelsExamined() {
		return totalModels;
	}
	
	////////////////////////////////////////////////////////////////////
	
	/**
	 * set up the array of Models to be run, using parameters drawn from the priors
	 */
	public void setup() {
		
		// create some example parameters for the model type from the priors
		List<Parameter> exampleParams = new ArrayList<Parameter>();
		for (int j = 0; j < modelParametersWithPriors.size(); j++) {
			exampleParams.add( modelParametersWithPriors.get(j).drawParameter() );
		}
		modelType.setParameters(exampleParams);
		
		// create an array of models to run
		models = ModelFactory.createModels(modelType, nreps);
		
		// draw parameters from the priors
		for (int i = 0; i <  nreps; i++) {
			
			List<Parameter> individualParams = new ArrayList<Parameter>();
			for (int j = 0; j < modelParametersWithPriors.size(); j++) {
				individualParams.add( modelParametersWithPriors.get(j).drawParameter() );
			}
			
			models[i].setParameters(individualParams);
			
			//System.out.println(models[i].toString());
		}
		
		//achievedStats = new ArrayList<List<Statistic>>();
		
	}
	
	/**
	 * run the array of models and collect the results statistics
	 * these models are just run in a loop but could parallelise out over many processors
	 */
	public void run() {
		/*
		if ( models == null ) {
			setup();
		}
		*/
		
		// could parallelise this out over many processors
		for (int i = 0; i < nreps; i++) {
			// run the model 
			models[i].run();
			
			/*
			if ( (i % 100 ) == 0 ) {
				System.out.println("Doing model "+i+" of "+nreps);
			}
			*/
		}
		
		
	}
	
	public void resetSummary() {

		goodModels			   = new ArrayList<Model>();
		goodResultStatistics   = new ArrayList<SummaryResultStatistic>();
		goodParameterEstimates = new ArrayList<SummaryResultStatistic>();
		totalModels			   = 0;
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	// FOR ALL  MODELS
	
	/**
	 * returns a summary (mean, stdev, min, max) of all models' result statistics regardless of whether in tolerance or not
	 * use for testing
	 * @return
	 */
	public List<SummaryResultStatistic> summariseAllModelResultStatistics() {
		
		List<SummaryResultStatistic> allResultStatistics = new ArrayList<SummaryResultStatistic>();
		// harvest all models
		
		for (int i = 0; i < nreps; i++) {
			List<Statistic> res = models[i].getStatistics();
			
			if (i==0) {
				int numStats = res.size();
				for (int j = 0; j < numStats; j++) {
					SummaryResultStatistic srs = new SummaryResultStatistic();
					srs.setName( res.get(j).getName() );
					srs.addResult( res.get(j) );
					allResultStatistics.add(srs);
				}
			} else {
				int numStats = allResultStatistics.size();
				for (int j = 0; j < numStats; j++) {
					allResultStatistics.get(j).addResult( res.get(j) );
				}
				
			}
			
		}
		
		for (int j = 0; j < allResultStatistics.size(); j++) {
			allResultStatistics.get(j).calculate();
		}
		
		return allResultStatistics;
		
	}
	

	/**
	 * returns a summary (mean, stdev, min, max) of all models' parameter estimates regardless of whether in tolerance or not
	 * use for testing
	 * @return
	 */
	public List<SummaryResultStatistic> summariseAllModelParameterEstimates() {
		
		List<SummaryResultStatistic> allParameterEstimates = new ArrayList<SummaryResultStatistic>();
		// harvest all models
		
		for (int i = 0; i < nreps; i++) {
			List<Parameter> res = models[i].getParameters();
			
			if (i==0) {
				int numStats = res.size();
				for (int j = 0; j < numStats; j++) {
					SummaryResultStatistic srs = new SummaryResultStatistic();
					srs.setName( res.get(j).getName() );
					srs.addResult( res.get(j) );
					allParameterEstimates.add(srs);
				}
			} else {
				int numStats = allParameterEstimates.size();
				for (int j = 0; j < numStats; j++) {
					allParameterEstimates.get(j).addResult( res.get(j) );
				}
				
			}
			
		}
		
		for (int j = 0; j < allParameterEstimates.size(); j++) {
			allParameterEstimates.get(j).calculate();
		}
		
		return allParameterEstimates;
		
	}
	
	/////////////////////////////////////////////////////////////////////////
	// FOR GOOD MODELS ONLY
	
	public void summariseGoodModels() {
		/*
		goodModels			   = new ArrayList<Model>();
		goodResultStatistics   = new ArrayList<SummaryResultStatistic>();
		goodParameterEstimates = new ArrayList<SummaryResultStatistic>();
		*/
		
		// harvest the good models
		for (int i = 0; i < nreps; i++) {
			totalModels++;								// count how many models have been examined
			
			if ( models[i].closeTo(targetStats) ) {
				goodModels.add(models[i]);
				
				List<Statistic> res = models[i].getStatistics();
				
				/*
				for (Statistic stat : res) {
					String statName = stat.getName();
					SummaryResultStatistic srs = new SummaryResultStatistic();
					srs.setName( statName ); 
					srs.addResult( stat );
					
					if (goodResultStatistics.contains(srs)) {
						int index = goodResultStatistics.indexOf(srs);
						goodResultStatistics.get(index).addResult(stat);
					}
				}
				*/
				if (goodResultStatistics.size() == 0) {
					int numStats = res.size();
					for (int j = 0; j < numStats; j++) {
						SummaryResultStatistic srs = new SummaryResultStatistic();
						srs.setName( res.get(j).getName() );
						srs.addResult( res.get(j) );
						goodResultStatistics.add(srs);
					}
				} else {
					int numStats = goodResultStatistics.size();
					for (int j = 0; j < numStats; j++) {
						goodResultStatistics.get(j).addResult( res.get(j) );
					}
				}
				
				List<Parameter> params = models[i].getParameters();
				
				if (goodParameterEstimates.size() == 0) {
					int numP = params.size();
					for (int j = 0; j < numP; j++) {
						SummaryResultStatistic srs = new SummaryResultStatistic();
						srs.setName( params.get(j).getName() );
						srs.addResult( params.get(j) );
						goodParameterEstimates.add(srs);
					} 
				} else {
					int numP = params.size();
					for (int j = 0; j < numP; j++) {
						goodParameterEstimates.get(j).addResult( params.get(j) );
					}
				}
				
			} //else {
				// temp for testing
			//	System.out.println("Unfortunately model "+models[i].toString()+" had results that were not close enough, distance from = "+models[i].distanceFrom(targetStats));
				
			//}
		}
		
		for (int j = 0; j < goodResultStatistics.size(); j++) {
			goodResultStatistics.get(j).calculate();
		}
		
		for (int j = 0; j < goodParameterEstimates.size(); j++) {
			goodParameterEstimates.get(j).calculate();
		}
		
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	
	public String toString() {
		String eol 		 = "\n";
		StringBuffer txt = new StringBuffer();
		
		txt.append("NumberOfModels="+nreps+eol);
		txt.append("ModelType="+modelType.getModelName()+eol);
		
		for (ParameterWithPrior mpp : modelParametersWithPriors) {
			txt.append(mpp+eol);
		}
		
		for (Statistic stat : targetStats) {
			txt.append(stat+eol);
		}
		
		return txt.toString();
	}
	
	
}
