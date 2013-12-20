package abc;

import java.util.*;

import priors.NormalPrior;
import priors.Prior;


/**
 * class to represent Summary result statistics (extra methods to result statistic)
 * can be used to summarise good parameters, or good result statistics
 * @author Samantha Lycett
 * @created 19 Oct 2013
 * @version 20 Oct 2013
 * @version 6 Dec 2013
 */
public class SummaryResultStatistic extends ResultStatistic {

	double minValue;
	double maxValue;
	double mean;
	double dev;
	List<Statistic> results = new ArrayList<Statistic>();
	boolean calculated 		= false;
	int	   N;
	
	public SummaryResultStatistic() {
		
	}
	
	public SummaryResultStatistic(String name, List<Statistic> results) {
		this.name = name;
		setResults(results);
	}
	
	///////////////////////////////////////////////////////////////////
	
	public void setResults(List<Statistic> results) {
		this.results = results;
		calculated   = false;
		calculate();
	}
	
	public void addResult(Statistic result) {
		this.results.add(result);
		calculated = false;
	}
	
	public void addResult(Parameter param) {
		ResultStatistic stat = new ResultStatistic(param.getName(), param.getValue());
		this.results.add(stat);
		calculated = false;
	}
	
	public double getMean() {
		calculate();
		return mean;
	}
	
	public double getDev() {
		calculate();
		return dev;
	}
	
	public double getMin() {
		calculate();
		return minValue;
	}
	
	public double getMax() {
		calculate();
		return maxValue;
	}
	
	public int getN() {
		calculate();
		return N;
	}
	
	///////////////////////////////////////////////////////////////////
	
	void calculate() {
		if (!calculated) {
			minValue	= Double.POSITIVE_INFINITY;
			maxValue	= Double.NEGATIVE_INFINITY;
			mean 		= 0;
			double x2  	= 0;
		
			for (int i = 0; i < results.size(); i++) {
				double x = results.get(i).getValue();
				mean += x;
				x2   += (x*x);
				
				if (x < minValue) {
					minValue = x;
				}
				if (x > maxValue) {
					maxValue = x;
				}
			}
		
			N 	 		= results.size();
			double nn 	= (double)N;
		
			mean 		= mean/nn;		// <x>
		
			x2   		= x2/nn;		// sqrt(N/(N-1)*(<x^2>-<x>^2))
			dev  		= Math.sqrt( ( nn/(nn-1) )*( x2 - (mean*mean) ) );
			calculated 	= true;
		
		}
	}
	

	///////////////////////////////////////////////////////////////////
	
	public ParameterWithPrior generateNewParameterWithNormalPriorFromResults() {
		calculate();
		Prior prior 		 = new NormalPrior(mean, dev, minValue-(3*dev), maxValue+(3*dev) );
		ParameterWithPrior p = new ParameterWithPrior(name, mean, prior);
		return p;
	}
	
	///////////////////////////////////////////////////////////////////
	
	public String toString() {
		String line = name + "\tN="+N+"\tmean="+mean+"\tstdev="+dev+"\tmin="+minValue+"\tmax="+maxValue;
		return line;
	}
	
	public String[] getColumnNames() {
		String[] colnames = {"N","mean","stdev","min","max"};
		return colnames;
	}
	
	public Double[] getColumnStats() {
		Double[] values = new Double[5];
		values[0] = (double)N;
		values[1] = mean;
		values[2] = dev;
		values[3] = minValue;
		values[4] = maxValue;
		return values;
	}
	
	public String[] getNamedColumnNames() {
		String[] colnames = {"name","N","mean","stdev","min","max"};
		return colnames;
	}
	
	public String[] getNamedColumnStats() {
		String[] cols = {name, ""+N, ""+mean, ""+dev, ""+minValue, ""+maxValue};
		return cols;
	}
	
}
