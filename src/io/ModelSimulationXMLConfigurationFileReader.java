package io;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import priors.FixedValuePrior;
import priors.Prior;
import priors.PriorFactory;
import abc.*;

/**
 * 
 * @author Samantha Lycett
 * @version 11 Dec 2013
 */
public class ModelSimulationXMLConfigurationFileReader {

	String 						filename;
	Document 					parametersXML;
	
	Model 					 modelType;
	List<ParameterWithPrior> priors;
	List<Parameter>			 modelSimulationParameters;
	List<TextParameter>		 generalParameters;
	
	boolean	verbose 			= true;
	
	public ModelSimulationXMLConfigurationFileReader() {
		
	}
	
	public void setFileName(String filename) {
		this.filename = filename;
	}
	
	public void parseFile() {
		readFile();
		parseDocument();
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	
	public String getFileName() {
		return filename;
	}

	
	/**
	 * return a template instance of the model type to simulate from
	 * @return
	 */
	public Model getModelType() {
		return modelType;
	}
	
	/**
	 * return the specific parameters for the model type, e.g. transmission term (b) + prior distribution
	 * @return
	 */
	public List<ParameterWithPrior> getPriors() {
		return priors;
	}
	
	/** 
	 * return the general model simulation parameters, e.g. numberOfModels
	 * @return
	 */
	public List<Parameter> getModelSimulationParameters() {
		return modelSimulationParameters;
	}
	
	public List<TextParameter> getGeneralParameters() {
		return generalParameters;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////
	
	void readFile() {

		DocumentBuilderFactory doc_factory = DocumentBuilderFactory.newInstance();

		try {

			// Using factory get an instance of document builder
			DocumentBuilder doc = doc_factory.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			parametersXML = doc.parse(filename);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void parseDocument(){
		// get the root elememt
		Element docEl 		= parametersXML.getDocumentElement();


		//System.out.println(docEl.getNodeName());
		if (!docEl.getNodeName().equals("LuckyGuess")) {
			System.out.println(this.getClass().getSimpleName()+".parseDocument - WARNING document type "+docEl.getNodeName()+" is not expected, but carrying on regardless");
		}

		////////////////////////////////////////////////////////////////
		// get the sub elements for General
		NodeList generalList = docEl.getElementsByTagName("General");
		
		if (generalList.getLength() != 1) {
			System.out.println("Sorry was only expecting 1 General in the XML, so just using the first and carrying on regardless");
		}

		Element generalEl	= (Element)generalList.item(0);
		parseGeneralElements( generalEl );
		

		////////////////////////////////////////////////////////////////
		// get the sub elements for ModelSimulation
		NodeList nodeList = docEl.getElementsByTagName("ModelSimulation");

		if (nodeList.getLength() != 1) {
			System.out.println("Sorry was only expecting 1 model in the XML, so just using the first and carrying on regardless");
		}

		int i  = 0;

		Element el		   = (Element)nodeList.item(i);
		
		parseModelType(el);
		parseParametersWithPriors(el);
		parseModelSimulationParameters(el);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void parseGeneralElements(Element generalEl) {
		
		this.generalParameters = new ArrayList<TextParameter>();
		NodeList paramList = generalEl.getElementsByTagName("parameter");
		
		// for each parameter
		for (int j = 0; j < paramList.getLength(); j++) {
			Node node 	 	= paramList.item(j);
			String name  	= ((Element)node).getAttributes().getNamedItem("name").getNodeValue();
			String value 	= ((Element)node).getAttributes().getNamedItem("value").getNodeValue();
			TextParameter p = new TextParameter( name, value );
			this.generalParameters.add(p);
			
			if (verbose) {
				System.out.println("GeneralParameter = "+p.toString());
			}
		}
		
	}

	void parseModelType(Element el) {
		NodeList modelList 	= el.getElementsByTagName("modelType");
		Node modelNode 		= modelList.item(0);
		String modelName	= ((Element)modelNode).getAttributes().getNamedItem("name").getNodeValue();
		this.modelType  	= ModelFactory.createModel(modelName);
		
		if (verbose) {
			System.out.println("ModelType = "+modelType.getModelName());
		}
	}
	
	void parseParametersWithPriors(Element el) {
		
		this.priors			= new ArrayList<ParameterWithPrior>();
		
		NodeList paramList = el.getElementsByTagName("modelParameter");
					
		// for each parameter
		for (int j = 0; j < paramList.getLength(); j++) {
			Node node 			 	= paramList.item(j);
						
			// get all components
			NamedNodeMap nodeAttribs= node.getAttributes();
			List<String> descripts  = new ArrayList<String>();
			List<String> values     = new ArrayList<String>();
						
			for (int k = 0; k < nodeAttribs.getLength(); k++) {
					Node 	n2 		= nodeAttribs.item(k);
					descripts.add(n2.getNodeName());
					values.add(n2.getNodeValue());
			}
						
			// get parameter name
			int nameIndex		  = descripts.indexOf("name");				
			String parameterName  = values.remove(nameIndex);
			descripts.remove(nameIndex);

			// get parameter value
			int valIndex		  = descripts.indexOf("value");
			Double parameterValue = Double.parseDouble(values.remove(valIndex));
			descripts.remove(valIndex);
						
			// get any priors
			int priorIndex		  = descripts.indexOf("prior");
			String priorType	  = "Fixed";
			if (priorIndex >= 0) {
					priorType		  = values.remove(priorIndex);
					descripts.remove(priorIndex);
			}
						
			Prior prior;
						
			if (priorType.equals("Fixed")) {
				prior = new FixedValuePrior(parameterValue);
			} else {
				prior = PriorFactory.createPrior(priorType);
				for (int k = 0; k < descripts.size(); k++) {
					Parameter pp = new Parameter(descripts.get(k), Double.parseDouble(values.get(k)) );
					prior.setParameter(pp);
				}
			}
						
			// create model parameter with prior
			ParameterWithPrior modelParameterWithPrior = new ParameterWithPrior(parameterName, parameterValue, prior);
			priors.add(modelParameterWithPrior);
			
			if (verbose) {
				System.out.println("ModelParameterWithPrior = "+modelParameterWithPrior.toString());
			}
		}
		
	}
	
	void parseModelSimulationParameters(Element el) {
		this.modelSimulationParameters = new ArrayList<Parameter>();
		NodeList paramList = el.getElementsByTagName("simulationParameter");
		
		// for each parameter
		for (int j = 0; j < paramList.getLength(); j++) {
			Node node 	 = paramList.item(j);
			String name  = ((Element)node).getAttributes().getNamedItem("name").getNodeValue();
			String value = ((Element)node).getAttributes().getNamedItem("value").getNodeValue();
			Double val   = Double.parseDouble(value);
			Parameter p = new Parameter( name, val );
			this.modelSimulationParameters.add(p);
			
			if (verbose) {
				System.out.println("SimulationParameter = "+p.toString());
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////
	
	/*
	public static void test() {
		String xmlName 						= "exampleConfigs//sir_modelSimulation.xml";
		ModelSimulationXMLConfigurationFileReader reader = new ModelSimulationXMLConfigurationFileReader();
		reader.setFileName(xmlName);
		reader.parseFile();
	}
	
	public static void main(String[] args) {
		test();
	}
	*/
	
	
}
