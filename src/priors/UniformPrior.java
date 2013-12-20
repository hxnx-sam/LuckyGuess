package priors;

import math.Distributions;
import abc.Parameter;

/**
 * Uniform prior distribution
 * @author sam
 * @created 19 Oct 2013
 * @version 19 Oct 2013
 * @version 6 Dec 2013
 *
 */
public class UniformPrior implements Prior {
	
	double lower = 0;
	double upper = 1;
	
	public UniformPrior() {
		
	}
	
	public UniformPrior(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;
	}
	
	@Override
	public boolean allowSet(Parameter p) {
		boolean allow 	= false;
		String[] names	= {"lower","upper"};
		for (String nn : names) {
			if ( p.getName().toLowerCase().contains(nn)) {
				allow = true;
			}
		}
		return allow;
	}

	@Override
	public void setParameter(Parameter p) {
		
		if (p.getName().toLowerCase().equals("upper")) {
			this.upper = p.getValue();
		} else if ( p.getName().toLowerCase().equals("lower") ) {
			this.lower = p.getValue();
		} else {
			System.out.println("UniformPrior.setParameter - sorry cant set parameter "+p.getName());
		}
		
	}

	@Override
	public Double draw() {
		double x = Distributions.randomUniform();
		x 		 = x*(upper-lower) + lower;
		return x;
	}
	
	public String toString() {
		String line = this.getClass().getSimpleName()+"\tlower="+lower+"\tupper="+upper;
		return line;
	}

}
