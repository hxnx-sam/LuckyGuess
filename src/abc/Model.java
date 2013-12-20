package abc;

import java.util.List;

/**
 * Interface to represent a Model
 * Models have parameters (setParameter, setParameters)
 * Models perform simulations (run)
 * Models generate statistics from their simulation results (getStatistics)
 * @author Samantha Lycett
 * @created 19 Oct 2013
 * @version 20 Oct 2013
 * @version 9 Dec 2013 - added getParameterNames, getModelName, calculateStatistics
 * @version 11 Dec 2013 - added run_traceToFile
 */
public interface Model {
	
	public String				getModelName();

	public void 				setParameter(Parameter p);
	public void					setParameters(List<Parameter> params);
	public List<Parameter> 		getParameters();
	public List<String>			getParameterNames();
	
	public List<Double[]> 		run();
	public void					calculateStatistics(List<Double[]> results);
	
	public void					run_traceToFile(String traceFileName);
	
	public List<Statistic> 		getStatistics();
	public boolean				closeTo(List<Statistic> targetStatistics);
	public double				distanceFrom(List<Statistic> targetStatistics);
	
}
