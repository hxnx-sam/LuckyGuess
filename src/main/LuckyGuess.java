package main;

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
 * @version 27 Dec 2013
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
