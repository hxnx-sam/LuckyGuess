package abc;

import priors.Prior;

/**
 * class to represent a parameter which has an updateable prior distribution
 * @author Samantha Lycett
 * @created 20 Oct 2013
 * @version 20 Oct 2013
 * @version 6 Dec 2013
 */
public class ParameterWithPrior extends Parameter {

	Prior 	prior;
	double 	oldValue;
	
	public ParameterWithPrior() {
		
	}
	
	public ParameterWithPrior(String name, double value) {
		super(name, value);
	}
	
	public ParameterWithPrior(String name, double value, Prior prior) {
		super(name, value);
		setPrior(prior);
	}
	
	////////////////////////////////////////////////////////////////////////////////
	
	public void setPrior(Prior prior) {
		this.prior = prior;
	}
	
	public void updatePriorParameter(Parameter p) {
		if (this.prior.allowSet(p)) {
			this.prior.setParameter(p);
		}
	}
	
	public void updatePriorMean(double d) {
		Parameter p = new Parameter("mean", d);
		if (this.prior.allowSet(p)) {
			this.prior.setParameter(p);
		}
	}
	
	public void updatePriorStdev(double d) {
		Parameter p = new Parameter("dev", d);
		if (this.prior.allowSet(p)) {
			this.prior.setParameter(p);
		}
	}
	
	/**
	 * draw a new value from the Prior
	 */
	public void newValue() {
		oldValue = value;
		value 	 = prior.draw();
	}
	
	/**
	 * revert to the previous value (new value was generated using newValue but it is no good)
	 */
	public void revertToPreviousValue() {
		value	 = oldValue;
	}
	
	/**
	 * returns the current value as a new Parameter object
	 * @return
	 */
	public Parameter getParameter() {
		Parameter pp = new Parameter(this.name, this.value);
		return pp;
	}
	
	/**
	 * draw a new parameter from the Prior and return as a new Parameter object
	 * @return
	 */
	public Parameter drawParameter() {
		Parameter pp = new Parameter(this.name, prior.draw() );
		return pp;
	}
	
	public String toString() {
		return (name + ":" + value+"\tPrior:"+prior);
	}
	
}
