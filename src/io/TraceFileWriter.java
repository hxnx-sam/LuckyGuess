package io;

import java.io.*;
import java.util.List;

/**
 * class to write a trace file, e.g. SIR trace from SIRModel to file
 * @author Samantha Lycett
 * @created 9 Dec 2013
 * @version 9 Dec 2013
 * @version 11 Dec 2013
 *
 */
public class TraceFileWriter {

	String 			filename;
	BufferedWriter 	outFile;
	String			delim  = "\t";
	
	/**
	 * empty constructor
	 */
	public TraceFileWriter()  {
	
	}
	
	/**
	 * constructor opens the output file
	 * @param filename
	 */
	public TraceFileWriter(String filename) {
		this.filename = filename;
		openFile();
	}
	
	/**
	 * all in one constructor, opens the output file, writes the header, column names and data, closes the file
	 * @param filename
	 * @param header
	 * @param colNames
	 * @param data
	 */
	public TraceFileWriter(String filename, String header, String[] colNames, List<Double[]> data) {
		this.filename = filename;
		openFile();
		writeHeader(header);
		writeColumnNames(colNames);
		writeData(data);
		closeFile();
	}
	
	////////////////////////////////////////////////////////////////////
	
	public void setDelimiter(String delim) {
		this.delim = delim;
	}
	
	public void openFile() {
		try {
			outFile = new BufferedWriter(new FileWriter(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeFile() {
		try {
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * writes each string element as one line - use this for multiline headers
	 * @param header
	 */
	public void writeHeader(List<String> header) {
		try {
			
			if (header != null) {
				for (String h : header) {
					outFile.write(h);
					outFile.newLine();
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * writes the header string as one line
	 * @param header
	 */
	public void writeHeader(String header) {
		try {
			
			if (header != null) {
				outFile.write(header);
				outFile.newLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * writes each element as a column header, i.e. all elements go on one line separated by the delimiter
	 * @param colNames
	 */
	public void writeColumnNames(String[] colNames) {
		
		try {
			
			if (colNames != null) {
			
				String line = colNames[0];
				for (int i = 1; i < colNames.length; i++) {
					line = line + delim + colNames[i];
				}
				outFile.write(line);
				outFile.newLine();
			
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * writes a single row of data
	 * should correspond to the header anems from writeColumnNames, but this is not enforced
	 * @param dd
	 */
	public void writeRow(Double[] dd) {
		
		try {
			if (dd != null) {
				
				String line = ""+dd[0].doubleValue();
				for (int i = 1; i < dd.length; i++) {
					line = line + delim + dd[i].doubleValue();
				}
				outFile.write(line);
				outFile.newLine();
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * writes a single row of data, preceeded by a row number
	 * should correspond to the header anems from writeColumnNames, but this is not enforced
	 * @param rowNumber
	 * @param dd
	 */
	public void writeRow(int rowNumber, Double[] dd) {
		
		try {
			if (dd != null) {
				
				String line = ""+rowNumber;
				for (int i = 0; i < dd.length; i++) {
					line = line + delim + dd[i].doubleValue();
				}
				outFile.write(line);
				outFile.newLine();
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * writes one line for each element of the list, but each list element contains an array of value values
	 * should correspond to the header names from writeColumnNames, but this is not enforced
	 * @param data
	 */
	public void writeData(List<Double[]> data) {
		
		try {
			
			for (Double[] dd : data) {
				
				if (dd != null) {
				
					String line = ""+dd[0].doubleValue();
					for (int i = 1; i < dd.length; i++) {
						line = line + delim + dd[i].doubleValue();
					}
					outFile.write(line);
					outFile.newLine();
				
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
		
	
}
