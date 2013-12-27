package inference;

import abc.*;
import java.util.List;

/**
 * General interface for classes that run models and accept or reject the parameters based on a target statistic
 * @author Samantha Lycett
 * @created 6 Dec 2013
 * @version 6 Dec 2013
 * @version 27 Dec 2013 - added output file names
 *
 */
public interface ModelInferer {

	public void setTargetStats(List<Statistic> targetStatistic);
	public void setModelParametersWithPriors(List<ParameterWithPrior> priors);
	public void setModelType(Model modelType);
	public void setNumModels(int nreps);
	
	public void run();
	
	public List<SummaryResultStatistic> getGoodParameterEstimates();
	public List<SummaryResultStatistic> getGoodResultStatistics();
	
	public void setOutputPath(String path);
	public void setOutputRootname(String rootname);
	
}
