package inference;

import java.util.List;

import math.Distributions;

import abc.Model;
import abc.Parameter;
import abc.ParameterWithPrior;
import abc.Statistic;
import abc.TextParameter;
import io.ModelInfererXMLConfigurationFileReader;

/**
 * factory class to create a model inferer based on the parameters in the xml configuration file
 * @author Samantha Lycett
 * @version 11 Dec 2013
 * @version 27 Dec 2013
 */
public class ModelInferenceFactory {
	
	/**
	 * creates a model inferer, from the parameters read in by the xml file
	 * @param xmlFile
	 * @return
	 */
	public static ModelInferer createModelInferer(ModelInfererXMLConfigurationFileReader xmlFile) {
		
		// general parameters
		List<TextParameter> generalParameters = xmlFile.getGeneralParameters();
		
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
		
		// model inference parameters
		String 						modelInferenceType	= xmlFile.getModelInferenceType();
		List<Parameter>				modelInferenceParameters = xmlFile.getModelInferenceParameters();
		List<Statistic> 		 	targetStatistics	= xmlFile.getTargetStatistics();
		Model					 	modelType			= xmlFile.getModelType();
		List<ParameterWithPrior> 	priors				= xmlFile.getPriors();
		
		ModelInferer inferer = null;
		
		Parameter temp 		= modelInferenceParameters.get(modelInferenceParameters.indexOf(new Parameter("numberOfModels", 0)));
		int numberOfModels 	= (int)temp.getValue();
		
		if (modelInferenceType.equals("Rejector")) {
			
			temp 		= modelInferenceParameters.get(modelInferenceParameters.indexOf(new Parameter("toleranceFraction", 0)));
			double toleranceFraction = temp.getValue();
			
			Rejector tempi = new Rejector(priors, targetStatistics, modelType, numberOfModels);
			tempi.setToleranceFraction(toleranceFraction);
			
			inferer = tempi;
			
		} else if (modelInferenceType.equals("StepDownRejector")) {
			
			temp 		= modelInferenceParameters.get(modelInferenceParameters.indexOf(new Parameter("maxIterations", 0)));
			int maxIterations = (int)temp.getValue();
			
			temp 		= modelInferenceParameters.get(modelInferenceParameters.indexOf(new Parameter("initialToleranceFraction", 0)));
			double initialToleranceFraction = temp.getValue();
			
			temp 		= modelInferenceParameters.get(modelInferenceParameters.indexOf(new Parameter("toleranceReductionFactor", 0)));
			double toleranceReductionFactor = temp.getValue();
			
			StepDownRejector tempi = new StepDownRejector(priors, targetStatistics, modelType, numberOfModels);
			tempi.setMaxIterations( maxIterations );
			tempi.setInitialToleranceFraction( initialToleranceFraction );
			tempi.setToleranceReductionFactor( toleranceReductionFactor );
			
			inferer = tempi;
			
			System.out.println(inferer.toString());
			
		}
		
		inferer.setOutputPath(path);
		inferer.setOutputRootname(rootname);
		
		return inferer;
	}

}
