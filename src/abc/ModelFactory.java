package abc;

import java.util.List;

import models.*;

/**
 * Factory class to generate Model(s)
 * @author Samantha Lycett
 * @created 20 Oct 2013
 * @version 20 Oct 2013
 *
 */
public class ModelFactory {

	/*
	static Model model = null;
	
	public void setModel(Model m) {
		model = m;
	}
	*/
	
	/**
	 * creates a new model instance (only default parameters) from the model name specified in the input string
	 * MODIFY THIS FUNCTION WHEN YOU WRITE A NEW MODEL CLASS
	 * ESPECIALLY IMPORTANT FOR XML CONFIGURATION FILES
	 * @param modelName
	 * @return
	 */
	public static Model createModel(String modelName) {
		if (modelName.equals("SimpleModel")) {
			SimpleModel newModel = new SimpleModel();
			return newModel;
		} else if (modelName.equals("SIRModel")) {
			SIRModel newModel = new SIRModel();
			return newModel;
		} else {
			System.out.println("Sorry the ModelFactory is not configured for "+modelName+" see createModel method");
			return null;
		}
	}
	
	/**
	 * creates a new Model instance with the same type and parameters as the input Model
	 * @param modelType
	 * @return
	 */
	public static Model createModel(Model modelType) {
		if (modelType == null) {
			System.out.println("ModelFactory.createModel - no model template set");
			return null;
			
		} else if (modelType instanceof SimpleModel) {
			
			List<Parameter> p 		= modelType.getParameters();
			SimpleModel newModel	= new SimpleModel();
			newModel.setParameters(p);
			return newModel;
			
		} else if (modelType instanceof SIRModel) {
			
			List<Parameter> p 		= modelType.getParameters();
			SIRModel newModel		= new SIRModel();
			newModel.setParameters(p);
			return newModel;
			
		} else {
			System.out.println("ModelFactor.createModel - sorry cant understand "+modelType.getClass() );
			return null;
		}
	}
	
	/**
	 * creates an Array of new Model(s) with the same type and parameters as the input model
	 * @param modelType
	 * @param n
	 * @return
	 */
	public static Model[] createModels(Model modelType, int n) {
		if (modelType == null) {
			System.out.println("ModelFactory.createModels - no model template set");
			return null;
			
		} else if (modelType instanceof SimpleModel) {
			
			//List<Parameter> p 		= modelType.getParamters();
			SimpleModel[] newModels	= new SimpleModel[n];
			for (int i = 0; i < n; i++) {
				//newModels[i] = new SimpleModel(p);
				newModels[i] = new SimpleModel();
			}
			return newModels;
			
		} else if (modelType instanceof SIRModel) {
			
			SIRModel[] newModels = new SIRModel[n];
			for (int i = 0; i < n; i++) {
				newModels[i] = new SIRModel();
			}
			return newModels;
			
		} else {
			System.out.println("ModelFactor.createModel - sorry cant understand "+modelType.getClass() );
			return null;
		}
	}
	
}
