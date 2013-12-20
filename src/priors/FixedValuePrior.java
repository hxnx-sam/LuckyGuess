package priors;

import abc.Parameter;

/**
 * Fixes the value of a parameter, useful for models which have parameters that dont change, e.g. dt in SIRModel
 * @author sam
 * @created 21 Oct 2013
 * @version 21 Oct 2013
 * @version 6 Dec 2013
 *
 */
public class FixedValuePrior implements Prior {
	
	Parameter param;
	
	public FixedValuePrior() {
		
	}
	
	public FixedValuePrior(double value) {
		param = new Parameter("FixedValue", value);
	}
	
	public FixedValuePrior(String name, Double value) {
		param = new Parameter(name, value);
	}
	
	public FixedValuePrior(Parameter p) {
		//setParameter(p);
		param = p;
	}

	@Override
	public boolean allowSet(Parameter p) {
		if (param != null) {
			//return p.getName().equals(param.getName());
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public void setParameter(Parameter p) {
		if (allowSet(p)) {
			param = p;
		}
	}

	@Override
	public Double draw() {
		return param.getValue();
	}
	
	public String toString() {
		String line = this.getClass().getSimpleName()+"\t"+param;
		return line;
	}

}
