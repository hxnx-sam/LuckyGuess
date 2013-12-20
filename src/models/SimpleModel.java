package models;

import io.TraceFileWriter;

import java.util.ArrayList;
import java.util.List;

import abc.*;

/**
 * A very simple model with just one parameter
 * Simplest toy implementation of Model I could think of
 * @author Samantha Lycett
 * @created 19 Oct 2013
 * @version 21 Oct 2013
 * @version 9 Dec 2013
 *
 */
public class SimpleModel implements Model {
	
	String 			modelName 	= this.getClass().getSimpleName();
	Parameter 		param 		= new Parameter("midpoint", 0);
	ResultStatistic result 		= new ResultStatistic();
	
	public SimpleModel() {
		
	}
	
	public SimpleModel(List<Parameter> p) {
		setParameters(p);
	}
	
	////////////////////////////////////////////////////////////////

	@Override
	public void setParameter(Parameter p) {
		this.param = p;
	}
	
	/**
	 * sets all the parameters for the model
	 * in this case there is only one parameter, so just use the first one in the list
	 */
	@Override
	public void setParameters(List<Parameter> p) {
		this.param = p.get(0);
	}
	

	@Override
	public List<Parameter> getParameters() {
		List<Parameter> paramList = new ArrayList<Parameter>();
		paramList.add(param);
		return paramList;
	}
	
	@Override
	public List<String> getParameterNames() {
		List<String> pn = new ArrayList<String>();
		pn.add(param.getName());
		return pn;
	}

	@Override
	public String getModelName() {
		return modelName;
	}
	
	/**
	 * runs the SimpleModel - in this case, the result is just the parameter
	 */
	@Override
	public List<Double[]> run() {
		result = new ResultStatistic();
		result.setName(param.getName());
		result.setValue(param.getValue());
		
		List<Double[]> dummyResult 	= new ArrayList<Double[]>();
		Double[] dd 				= { param.getValue() };
		dummyResult.add( dd );
		return dummyResult;
	}

	/**
	 * calculates the average of the input data and stores in the SimpleModel results
	 */
	@Override
	public void calculateStatistics(List<Double[]> data) {
		
		// just average the input data
		int count 		= 0;
		double average 	= 0;
		for (Double[] dd : data) {
			for (Double d : dd) {
				average += d;
				count++;
			}
		}
		average = average/(double)count;
		result = new ResultStatistic("average", average);
		
	}
	
	/**
	 * returns the results of the Simple Model as a list of statistics
	 */
	@Override
	public List<Statistic> getStatistics() {
		List<Statistic> stats = new ArrayList<Statistic>();
		stats.add(result);
		return stats;
	}
	
	/**
	 * returns true if the result is within the tolerance of the first targetStatistic in the list
	 */
	@Override
	public boolean closeTo(List<Statistic> targetStatistics) {
		boolean near = targetStatistics.get(0).closeTo(result);
		return near;
	}
	
	/**
	 * returns the distance (absolute difference) between the first target statistic in the list and this result value
	 */
	@Override
	public double distanceFrom(List<Statistic> targetStatistics) {
		double dist = targetStatistics.get(0).distanceFrom(result);
		return dist;
	}
	
	/**
	 * returns all parameters on one line, separated by tab delimiter
	 * @return
	 */
	public String toString() {
		StringBuffer txt = new StringBuffer();
		String delim 	 = "\t";
		txt.append("Model="+modelName+delim);
		txt.append(param.getName()+"="+param.getValue());
		return txt.toString();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * runs the model with current parameters, and outputs the traces to file (in this case it is just a single value), also calculates the statistics from the results.
	 */
	@Override
	public void run_traceToFile(String traceFileName) {
		// run the model
		List<Double[]> results = run();
				
		// always calculate the statistics anyway
		calculateStatistics(results);
		
		// write output to file
		String 			header 		= "#"+toString();
		String[] 		colNames 	= {param.getName()};
		new TraceFileWriter(traceFileName, header, colNames, results);
	}
	
}
