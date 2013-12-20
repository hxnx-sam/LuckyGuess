package main;

import java.util.ArrayList;
import java.util.List;

import priors.FixedValuePrior;
import priors.NormalPrior;
import math.Distributions;
import models.SIRModel;
import abc.Model;
import abc.ParameterWithPrior;
import abc.ResultStatistic;
import abc.Statistic;
import abc.TextParameter;
import io.*;
import inference.*;

/**
 *  This is LuckyGuess.
 *  It performs ABC parameter estimation over SIR-like models
 *  
 *  Copyright (C) 2013  Samantha Lycett
 *  
 *  This file is part of LuckyGuess.
 *
 *  LuckyGuess is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  LuckyGuess is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with LuckyGuess.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * LuckyGuess - this is the main class, it reads the configuration xmlfile and runs the inference
 * @author Samantha Lycett
 * @version 20 Dec 2013
 * 
 * 20 Dec 2013 - status - runs with example file xml but some parts still hard coded
 * 20 Dec 2013 - TO DO - fix the initial target statistics settings from the xmlfile (not this class - ModelInfererXMLConfigurationFileReader needs fixing)
 * 20 Dec 2013 - TO DO - implement output files
 *
 */
public class LuckyGuess {

	ModelInfererXMLConfigurationFileReader 	xmlFile;
	ModelInferer 							inferer;
	
	public LuckyGuess() {
		
	}
	
	public void initialise(String xmlFileName) {
		xmlFile = new ModelInfererXMLConfigurationFileReader();
		xmlFile.setFileName(xmlFileName);
		xmlFile.parseFile();
		
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
		
		// temp remove for testing
		//inferer = ModelInferenceFactory.createModelInferer(xmlFile);
		
		//List<ParameterWithPrior> 	modelParametersWithPriors 	= xmlFile.getPriors();
		/*
		List<ParameterWithPrior> modelParametersWithPriors = new ArrayList<ParameterWithPrior>();
		modelParametersWithPriors.add( new ParameterWithPrior("N", 1000, new FixedValuePrior(1000) ) );
		modelParametersWithPriors.add( new ParameterWithPrior("b", 2,    new NormalPrior(2.0, 1.0, 0.5, 2.5)) );
		modelParametersWithPriors.add( new ParameterWithPrior("y", 1,	new NormalPrior(1.0, 1.0, 0.5, 2.5)) );
		modelParametersWithPriors.add( new ParameterWithPrior("initialI", 1, new FixedValuePrior(1)));
		modelParametersWithPriors.add( new ParameterWithPrior("dt", 0.1, new FixedValuePrior(0.1)));
		modelParametersWithPriors.add( new ParameterWithPrior("maxReps", 1000, new FixedValuePrior(1000)));
		*/
		
		/*
		List<Statistic> 			targetStatistics2 			= xmlFile.getTargetStatistics();
		System.out.println("From input file:");
		for (Statistic stat : targetStatistics2) {
			System.out.println(stat.toString());
		}
		*/
		
		/*
		 * Ran the SIR Model to generate data with target statistics:
		peakInfecteds:157.65906536006693	tol:15.765906536006694
		timeOfPeak:6.999999999999991	tol:0.6999999999999992
		areaUnderCurveS:25011.945628255187	tol:2501.194562825519
		areaUnderCurveI:802.8383446723763	tol:80.28383446723763
		areaUnderCurveR:74385.21602707192	tol:7438.521602707192
		 */
		
		// these ones are OK
		/*
		List<Statistic>				targetStatistics = new ArrayList<Statistic>();
		targetStatistics.add(new ResultStatistic("peakInfecteds", 157.659, 16.0));
		targetStatistics.add(new ResultStatistic("timeOfPeak", 7.0, 0.7));
		targetStatistics.add(new ResultStatistic("areaUnderCurveS", 25011.95, 2501.0));
		targetStatistics.add(new ResultStatistic("areaUnderCurveI", 802.84, 80.2));
		targetStatistics.add(new ResultStatistic("areaUnderCurveR", 74385.21, 7438.5));
		System.out.println("Hard coded");
		for (Statistic stat : targetStatistics) {
			System.out.println(stat.toString());
		}
		*/
		
		/*
		Model 						modelType 					= xmlFile.getModelType();
		int 						numModelsPerBatch			= 100;
		
		StepDownRejector.setVerbose(true);
		inferer = new StepDownRejector(modelParametersWithPriors, targetStatistics, modelType, numModelsPerBatch);
		((StepDownRejector)inferer).setMaxIterations(10);
		((StepDownRejector)inferer).setInitialToleranceFraction(0.5);
		((StepDownRejector)inferer).setToleranceReductionFactor(0.8);
		*/
		
		inferer = ModelInferenceFactory.createModelInferer(xmlFile);
		
		System.out.println("-----------------");
		System.out.println(inferer.toString());
	}
	
	public void run() {
		System.out.println("------------------");
		inferer.run();
		System.out.println("------------------");
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static void test() {
		String xmlName 						= "exampleConfigs//sirmodel.xml";
		LuckyGuess luckyGuess = new LuckyGuess();
		luckyGuess.initialise(xmlName);
		luckyGuess.run();
	}
	
	public static void main(String[] args) {
		System.out.println("** LuckyGuess **");
		
		test();
		
		System.out.println("** END **");
	}
	
}
