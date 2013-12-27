package models;

import io.TraceFileWriter;

import java.util.ArrayList;
import java.util.List;

import abc.*;

/**
 * Explicit Epidemilogical SIR model, use for functional testing
 * see quickSIR.R (R code)
 * @author Samantha Lycett
 * @created 21 Oct 2013
 * @version 21 Oct 2013
 * @version 9 Dec 2013
 *
 */
public class SIRModel implements Model {
	
	String	modelName			= this.getClass().getSimpleName();//"SIRModel";

	double 			N 			= 1000;
	double 			b 			= 2.0;
	double 			y 			= 1.0;
	double  		T			= 0;
	double 			S 			= 0;
	double 			I 			= 0;
	double 			R 			= 0;
	double 			dt 			= 0.01;
	int 			maxReps 	= 5000;
	double 			initialI 	= 1;
	//List<Double[]> 	results;
	List<Statistic> stats;
	
	public SIRModel() {
		
	}
	
	public SIRModel(List<Parameter> params) {
		setParameters(params);
	}
	
	///////////////////////////////////////////////////////////////////////
	
	private void updateSIR() {
		
		double bSI = b*S*I/N;
		double yI  = y*I;
		double dS  = -bSI;
		double dI  = bSI - yI;
		double dR  = yI;
		
		T = T + dt;
		S = S + (dS*dt);
		I = I + (dI*dt);
		R = R + (dR*dt);
	}
	
	private List<Double[]> runSIR() {
		
		I = initialI;
		S = N - I;
		R = 0;
		
		List<Double[]> results = new ArrayList<Double[]>();
		int count = 0;
		results.add(new Double[] {T,S,I,R} );
		
		while ( (count <= maxReps) && (R < N) && (I < N) && (S >= 0) ) {
			updateSIR();
			results.add( new Double[] {T,S,I,R} );
			count++;
		}
		
		return results;
	}

	@Override
	public void setParameter(Parameter p) {
		if (p.getName().equals("N")) {
			N 		 = p.getValue();
		} else if (p.getName().equals("initialI")) {
			initialI = p.getValue();
		} else if (p.getName().equals("b")) {
			b = p.getValue();
		} else if (p.getName().equals("y")) {
			y = p.getValue();
		} else if (p.getName().equals("maxReps")) {
			maxReps = (int)(double)p.getValue();
		} else if (p.getName().equals("dt")) {
			dt = p.getValue();
		} else {
			System.out.println("SIRModel.setParameter - sorry cant understand "+p.getName());
		}
		
	}

	@Override
	public void setParameters(List<Parameter> params) {
		for (Parameter p : params) {
			setParameter(p);
		}
		
	}

	@Override
	public List<Parameter> getParameters() {
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("N", N));
		params.add(new Parameter("initialI", initialI));
		params.add(new Parameter("b", b));
		params.add(new Parameter("y", y));
		params.add(new Parameter("dt", dt));
		params.add(new Parameter("maxReps", (double)maxReps));
		return params;
	}
	
	@Override
	public List<String> getParameterNames() {
		List<String> pn = new ArrayList<String>();
		pn.add("N");
		pn.add("initialI");
		pn.add("b");
		pn.add("y");
		pn.add("dt");
		pn.add("maxReps");
		return pn;
	}
	
	@Override
	public String getModelName() {
		return modelName;
	}

	@Override
	public List<Double[]> run() {
		// run the model
		List<Double[]> results = runSIR();
		
		// always calculate the statistics
		calculateStatistics(results);
		
		return results;
	}
	
	
	@Override
	public void calculateStatistics(List<Double[]> results) {
		//if (stats == null) {
			
			// calculate peak infecteds, time of peak, areaUnderCurveI, areaUnderCurveR ?
			double peakInfecteds 	= 0;
			double timeOfPeak    	= 0;
			double areaUnderCurveS	= 0;
			double areaUnderCurveI 	= 0;
			double areaUnderCurveR  = 0;
		
			for (Double[] tsir : results) {
				double t = tsir[0];
				double s = tsir[1];
				double i = tsir[2];
				double r = tsir[3];
			
				if (i > peakInfecteds) {
					peakInfecteds 	= i;
					timeOfPeak 		= t;
				}
			
				areaUnderCurveS += s;
				areaUnderCurveI += i;
				areaUnderCurveR += r;
			}
			
			areaUnderCurveS = areaUnderCurveS*dt;
			areaUnderCurveI = areaUnderCurveI*dt;
			areaUnderCurveR = areaUnderCurveR*dt;
		
			stats = new ArrayList<Statistic>();
			stats.add(new ResultStatistic("peakInfecteds", peakInfecteds) );
			stats.add(new ResultStatistic("timeOfPeak", timeOfPeak) );
			stats.add(new ResultStatistic("areaUnderCurveS", areaUnderCurveS));
			stats.add(new ResultStatistic("areaUnderCurveI", areaUnderCurveI));
			stats.add(new ResultStatistic("areaUnderCurveR", areaUnderCurveR));
		
		//}
		
	}
	

	@Override
	public List<Statistic> getStatistics() {
		//calculateStatistics();
		return stats;
	}

	/**
	 * returns true IFF ALL of the results statistics are within the respective tolerances of the target statistics
	 */
	@Override
	public boolean closeTo(List<Statistic> targetStatistics) {
		boolean near = true;
		
		for (Statistic target : targetStatistics) {
			// find the result statistic corresponding to the target statistic
			int index 					= stats.indexOf(target);
			if (index >= 0) {
				ResultStatistic resultStat 	= (ResultStatistic)stats.get(index);
			
				// compare the statistics
				boolean  resultNear			= ((ResultStatistic)target).closeTo(resultStat);
			
				// near if all the statistics are near
				near 						= near && resultNear;
			} else {
				System.out.println(modelName+".closeTo - warning no result statistic for "+target.getName());
			}
		}
		
		return near;
	}
	
	/**
	 * returns the squared euclidean distance of normalised target statistics - e.g. ((x1-x2)/x2)^2 + ((y1-y2)/y2)^2
	 */
	@Override
	public double distanceFrom(List<Statistic> targetStatistics) {
		
		double dist = 0;
		
		for (Statistic target : targetStatistics) {
			// find the result statistic corresponding to the target statistic
			int index 					= stats.indexOf(target);
			if (index >= 0) {
				ResultStatistic resultStat 	= (ResultStatistic)stats.get(index);
			
				// compare the statistics
				//double  resultdist			= ((ResultStatistic)target).distanceFrom(resultStat);
				double  resultdist			= resultStat.distanceFrom(target);
				resultdist					= resultdist/target.getValue();
				dist					   += (resultdist*resultdist);
			} else {
				System.out.println("SIRModel.closeTo - warning no result statistic for "+target.getName());
			}
		}
		
		return dist;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * runs the model with current parameters, and outputs the SIR traces to file, also calculates the statistics from the results.
	 */
	@Override
	public void run_traceToFile(String traceFileName) {
		// run the model
		List<Double[]> results = runSIR();
				
		// always calculate the statistics anyway
		calculateStatistics(results);
		
		// write output to file
		String 			header 		= "#"+toString();
		String[] 		colNames 	= {"T","S","I","R"};
		new TraceFileWriter(traceFileName, header, colNames, results);
	}
	
	/**
	 * outputs the current statistics to file
	 * typically use immediately after run_traceToFile
	 */
	/*
	@Override
	public void stats_ToFile(String statsFileName) {
		// write results statistics to file
		String header 		= "#"+toString();
		
		String[] colNames	= new String[stats.size()];
		List<Double[]> vals = new ArrayList<Double[]>();
		for (int i = 0; i < stats.size(); i++) {
			colNames[i] = stats.get(i).getName();
			Double[] dd = new Double[1];
			dd[0]       = stats.get(i).getValue();
		}
		
		new TraceFileWriter(statsFileName, header, colNames, vals);
	}
	*/
	
	/**
	 * returns all parameters on one line, separated by tab delimiter
	 * @return
	 */
	public String toString() {
		StringBuffer txt = new StringBuffer();
		String delim 	 = "\t";
		txt.append("Model="+modelName+delim);
		txt.append("N="+N+delim);
		txt.append("b="+b+delim);
		txt.append("y="+y+delim);
		txt.append("dt="+dt+delim);
		txt.append("maxReps="+maxReps+delim);
		txt.append("initialI="+initialI);
		return txt.toString();
	}
	
}
