package main;

import io.*;
import abc.*;

import java.util.*;

import math.Distributions;

/**
 * class to simulate data from a model, as specified in the input xml file,
 * contains a "main" method.
 * Use this to generate data for testing LuckyGuess - it will output test data in the correct format.
 * @author  Samantha Lycett
 * @version 11 Dec 2013
 * 
 * 20 DEC 2013 - example input xml file hardcoded for testing
 *
 */
public class LuckySimulation {

	ModelSimulationXMLConfigurationFileReader xmlFile;
	
	
	/**
	 * empty constructor
	 */
	public LuckySimulation() {
		
	}
	
	/**
	 * all in one constructor, reads xml configuration file and runs the model
	 * @param xmlFileName
	 */
	public LuckySimulation(String xmlFileName) {
		initialise(xmlFileName);
		run();
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void initialise(String xmlFileName) {
		xmlFile = new ModelSimulationXMLConfigurationFileReader();
		xmlFile.setFileName(xmlFileName);
		xmlFile.parseFile();
	}
	
	public void run() {
		
		Model modelType 					  		= xmlFile.getModelType();
		List<TextParameter> generalParameters 		= xmlFile.getGeneralParameters();
		List<Parameter> 	simParameters 			= xmlFile.getModelSimulationParameters();
		List<ParameterWithPrior> modelParameters	= xmlFile.getPriors();
		
		String path 		= generalParameters.get(generalParameters.indexOf(new TextParameter("path","x"))).getValue();
		String rootname		= generalParameters.get(generalParameters.indexOf(new TextParameter("rootname","x"))).getValue();
		String seedTxt		= generalParameters.get(generalParameters.indexOf(new TextParameter("seed","x"))).getValue();
		long seed;
		try {
			seed		= Long.parseLong(seedTxt);
			Distributions.initialiseWithSeed((int)seed);
		} catch (NumberFormatException e) {
			seed		= Distributions.initialise();
		}
		
		
		Parameter temp 		= simParameters.get(simParameters.indexOf(new Parameter("numberOfModels", 0)));
		int numberOfModels 	= (int)temp.getValue();
		
		temp				= simParameters.get(simParameters.indexOf(new Parameter("replicatesPerModel", 0)));
		int nreps			= (int)temp.getValue();
		
		String paramsFileName 	= path + rootname + "_params_stats_summary.txt";
		TraceFileWriter outFile = new TraceFileWriter(paramsFileName);
		
		for (int i = 0; i < numberOfModels; i++) {
			
			// draw some parameters from the distribution of parameters
			List<Parameter> params 	=  new ArrayList<Parameter>();
			
			for (ParameterWithPrior mpp : modelParameters) {
				params.add( mpp.drawParameter() );
			}
			
			/*
			// one stats file per model - contains many replicates of the model with the same parameters
			String statsName				= path + rootname + "_stats" + i + ".txt";
			TraceFileWriter	statsWriter		= new TraceFileWriter(statsName);
			*/
			
			for (int j = 0; j < nreps; j++) {
			
				// run a model with these parameters
				Model simModel 			= ModelFactory.createModel(modelType);
				simModel.setParameters(params);
				
			
				// output one trace file for each replicate of each model
				String traceFileName  	= path + rootname + "_trace" + i + "_replicate" + j + ".txt";
				simModel.run_traceToFile(traceFileName);
				
				// collect statistics and write as a single single in stats output file
				List<Statistic> stats 	= simModel.getStatistics();
			
				/*
				if (j==0) {
					// write results statistics to file
					String header 		= "#"+simModel.toString();
					
					String[] colNames	= new String[stats.size()+1];
					colNames[0]		= "Replicate";
					
					System.out.print(colNames[0]);
					
					for (int k = 0; k < stats.size(); k++) {
						colNames[k+1] = stats.get(k).getName();
						
						System.out.print("\t"+colNames[k+1]);
					}
					System.out.println();
					
					statsWriter.writeHeader(header);
					statsWriter.writeColumnNames(colNames);
				}
				

				Double[] statsVals  = new Double[stats.size()];
				System.out.print(j);
				for (int k = 0; k < stats.size(); k++) {
					statsVals[k] = stats.get(k).getValue();
					
					System.out.print("\t"+statsVals[k]);
				}
				System.out.println();
				
				statsWriter.writeRow(j, statsVals);
				*/
				
				

				// compose summary data
				int ncols 	  		= 1 + 1+ modelParameters.size() + stats.size();
				String[] colNames	= new String[ncols];
				Double[] summaryVals = new Double[ncols];
				colNames[0]			= "Model";
				colNames[1]			= "Replicate";
				summaryVals[0]		= (double)i;
				summaryVals[1]		= (double)j;
				int count			= 2;
					
				for (Parameter mp : simModel.getParameters() ) {
					colNames[count] 		= mp.getName();
					summaryVals[count]		= mp.getValue();
					count++;
				}
					
				for (Statistic stat : stats) {
					colNames[count] 	= stat.getName();
					summaryVals[count]	= stat.getValue();
					count++;
				}
					
				// write header parameters and stats summary file
				if ((i==0) && (j==0)) {
					String header = "#" + this.getClass().getSimpleName() + "\tconfigFile="+xmlFile.getFileName()+"\tseed="+seed;
					outFile.writeHeader(header);
					outFile.writeColumnNames(colNames);	
				}
				
				// write summary values
				outFile.writeRow(summaryVals);
				
			} // end of replicates per model
			
			//statsWriter.closeFile();
			
		} // end of models
		
		outFile.closeFile();
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void test() {
		String xmlFileName = "exampleConfigs//example_sir_modelSimulation.xml";
		new LuckySimulation(xmlFileName);
	}
	
	public static void main(String[] args) {
		System.out.println("** LuckySimulation **");
		
		test();
		
		System.out.println("** END **");
	}
	
}
