package io;

import java.io.*;
import java.util.List;

import abc.*;

/**
 * class to write the parameters and target statistics from a model in xml format
 * this is useful for creating LuckyGuess input xmls, and is used per model in LuckySimulation
 * @author Samantha Lycett
 * @created 27 Dec 2013
 * @version 27 Dec 2013
 *
 */
public class ModelXMLOutput {

	String 			filename;
	BufferedWriter 	outfile;
	String 			indent = "\t\t";
	Model			theModel;
	
	/**
	 * empty constructor
	 */
	public ModelXMLOutput() {
		
	}
	
	/**
	 * all in one constructor, opens file, writes model details, closes file
	 * @param filename
	 * @param m
	 */
	public ModelXMLOutput(String filename, Model m) {
		this.filename = filename;
		this.theModel = m;
		
		writeModelDetails();
		
	}

	/////////////////////////////////////////////////////////////////////////////
	
	/**
	 * opens file, writes model details, closes file
	 */
	public void writeModelDetails() {
		openFile();
		writeModelType();
		writeModelParameters();
		writeTargetStatistics();
		closeFile();
	}
	
	/////////////////////////////////////////////////////////////////////////////
	
	public void setFilename(String fn) {
		this.filename = fn;
	}
	
	public void setModel(Model m) {
		this.theModel = m;
	}
	
	public void setNumberOfIndents(int i) {
		String ii = "";
		for (int j = 0; j < i; j++) {
			ii = ii + "\t";
		}
		
		this.indent = ii;
	}
	
	/////////////////////////////////////////////////////////////////////////////
	
	void openFile() {
		try {
			outfile = new BufferedWriter(new FileWriter(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void closeFile() {
		try {
			outfile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void writeModelType() {
		String line = indent + "<modelType\tname=\""+theModel.getModelName()+"\"/>";
		try {
			outfile.write(line);
			outfile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	void writeModelParameters() {
		List<Parameter> params = theModel.getParameters();
		
		try {
		
			for (Parameter p : params) {
				String line = indent + "<modelParameter\tname=\""+p.getName()+"\"\tvalue=\""+p.getValue()+"\"/>";
				outfile.write(line);
				outfile.newLine();
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	void writeTargetStatistics() {
		List<Statistic> stats = theModel.getStatistics();
		
		try {
			
			for (Statistic stat : stats) {
				String line = indent + 
								"<targetStatistic\tname=\""+stat.getName()+
								"\"\tvalue=\""+stat.getValue()+
								"\"\ttolerance=\""+stat.getTolerance()+"\"/>";
				
				outfile.write(line);
				outfile.newLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
