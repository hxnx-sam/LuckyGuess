package io;

import java.util.List;

import abc.Model;
import abc.Parameter;
import abc.Statistic;

/**
 * Class to compose row information of model parameters and statistics
 * output can be used in TraceFileWriter
 * use this class to compose values in LuckySimulation and LuckyGuess
 * @author Samantha Lycett
 * @created 27 Dec 2013
 * @version 27 Dec 2013
 *
 */
public class ModelSingleLineOutput {
	
	// Class methods
	
	/**
	 * returns an array of column headers, including ModelNumber and ReplicateNumber
	 * @param col1
	 * @param col2
	 * @param theModel
	 * @return
	 */
	public static String[] composeValuesHeader(String col1, String col2, Model theModel) {
		
		List<Parameter> params = theModel.getParameters();
		List<Statistic> stats  = theModel.getStatistics();
		
		// compose summary data
		int ncols 	  			= 1 + 1 + params.size() + stats.size();
		String[] colNames		= new String[ncols];
		//Double[] vals 			= new Double[ncols];
		colNames[0]				= col1;	//"Model";
		colNames[1]				= col2;	//"Replicate";
		//vals[0]					= (double)i;
		//vals[1]					= (double)j;
		int count				= 2;
			
		for (Parameter mp : params ) {
			colNames[count] 	= mp.getName();
			//vals[count]			= mp.getValue();
			count++;
		}
			
		for (Statistic stat : stats) {
			colNames[count] 	= stat.getName();
			//vals[count]			= stat.getValue();
			count++;
		}
		
		return colNames;
		
	}
	

	/**
	 * returns array of model number, replicate number, model parameters, model statistics
	 * @param i
	 * @param j
	 * @param theModel
	 * @return
	 */
	public static Double[] composeValuesLine(int i, int j, Model theModel) {
		
		List<Parameter> params = theModel.getParameters();
		List<Statistic> stats  = theModel.getStatistics();
		
		// compose summary data
		int ncols 	  			= 1 + 1 + params.size() + stats.size();
		//String[] colNames		= new String[ncols];
		Double[] vals 			= new Double[ncols];
		//colNames[0]				= "Model";
		//colNames[1]				= "Replicate";
		vals[0]					= (double)i;
		vals[1]					= (double)j;
		int count				= 2;
			
		for (Parameter mp : params ) {
			//colNames[count] 	= mp.getName();
			vals[count]			= mp.getValue();
			count++;
		}
			
		for (Statistic stat : stats) {
			//colNames[count] 	= stat.getName();
			vals[count]			= stat.getValue();
			count++;
		}
		
		return vals;
		
	}
	
	///////////////////////////////////////////////////////////////////////////
	// INCLUDES TARGET STATISTICS
	// useful for BatchOfModels
	
	/**
	 * returns an array of column headers, including ModelNumber and ReplicateNumber, and distance from target statistics and accepted or not
	 * @param col1
	 * @param col2
	 * @param theModel
	 * @param targetStatistics
	 * @return
	 */
	public static String[] composeValuesHeader(String col1, String col2, Model theModel, List<Statistic> targetStatistics) {
		
		List<Parameter> params = theModel.getParameters();
		List<Statistic> stats  = theModel.getStatistics();
		
		// compose summary data
		int ncols 	  			= 1 + 1 + params.size() + stats.size() + 1 + 1;
		String[] colNames		= new String[ncols];
		//Double[] vals 			= new Double[ncols];
		colNames[0]				= col1;	//"Model";
		colNames[1]				= col2;	//"Replicate";
		//vals[0]					= (double)i;
		//vals[1]					= (double)j;
		int count				= 2;
			
		for (Parameter mp : params ) {
			colNames[count] 	= mp.getName();
			//vals[count]			= mp.getValue();
			count++;
		}
			
		for (Statistic stat : stats) {
			colNames[count] 	= stat.getName();
			//vals[count]			= stat.getValue();
			count++;
		}
		
		colNames[count] = "DistanceFromTarget";
		
		count++;
		colNames[count] = "WithinTolerance";
		
		return colNames;
		
	}
	

	/**
	 * returns array of model number, replicate number, model parameters, model statistics, distance from target statistics and closeTo
	 * @param i
	 * @param j
	 * @param theModel
	 * @param targetStatistics
	 * @return
	 */
	public static Double[] composeValuesLine(int i, int j, Model theModel, List<Statistic> targetStatistics) {
		
		List<Parameter> params = theModel.getParameters();
		List<Statistic> stats  = theModel.getStatistics();
		
		// compose summary data
		int ncols 	  			= 1 + 1 + params.size() + stats.size() + 1 + 1;
		//String[] colNames		= new String[ncols];
		Double[] vals 			= new Double[ncols];
		//colNames[0]				= "Model";
		//colNames[1]				= "Replicate";
		vals[0]					= (double)i;
		vals[1]					= (double)j;
		int count				= 2;
			
		for (Parameter mp : params ) {
			//colNames[count] 	= mp.getName();
			vals[count]			= mp.getValue();
			count++;
		}
			
		for (Statistic stat : stats) {
			//colNames[count] 	= stat.getName();
			vals[count]			= stat.getValue();
			count++;
		}
		
		vals[count] = theModel.distanceFrom(targetStatistics);
		
		count++;
		boolean closeTo = theModel.closeTo(targetStatistics);
		if (closeTo) {
			vals[count] = 1.0;
		} else {
			vals[count] = 0.0;
		}
		
		return vals;
		
	}
	
	
}
