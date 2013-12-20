package priors;

import math.Distributions;
import abc.Parameter;

/**
 * Normal (gaussian) prior distribution
 * @author Samantha Lycett
 * @created 19 Oct 2013
 * @version 19 Oct 2013
 *
 */
public class NormalPrior implements Prior {
	
	double meanValue  = 0;
	double stdevValue = 1;
	double lower	  = Double.NEGATIVE_INFINITY;
	double upper      = Double.POSITIVE_INFINITY;
	
	public NormalPrior() {
		
	}
	
	public NormalPrior(double meanValue, double devValue) {
		this.meanValue  = meanValue;
		this.stdevValue = devValue;
	}
	
	public NormalPrior(double meanValue, double devValue, double lower, double upper) {
		this.meanValue = meanValue;
		this.stdevValue= devValue;
		this.lower     = lower;
		this.upper     = upper;
	}

	@Override
	public boolean allowSet(Parameter p) {
		boolean allow 	= false;
		String[] names	= {"mean","dev","lower","upper"};
		for (String nn : names) {
			if ( p.getName().toLowerCase().contains(nn)) {
				allow = true;
			}
		}
		return allow;
	}
	
	@Override
	public void setParameter(Parameter p) {
		if (p.getName().toLowerCase().startsWith("mean")) {
			meanValue = p.getValue();
		} else if (p.getName().toLowerCase().contains("dev")) {
			stdevValue = p.getValue();
		} else if (p.getName().toLowerCase().equals("lower")) {
			lower = p.getValue();
		} else if (p.getName().toLowerCase().equals("upper")) {
			upper = p.getValue();
		} else {
			System.out.println("NormalPrior.setParameter - sorry cant set "+p.getName());
		}
		
	}

	@Override
	public Double draw() {
		double x 	  = meanValue;
		boolean again = true;
		int maxtries  =  1000;
		int count	  = 0;
		
		while (again) {
			x 		 = Distributions.randomGaussian();
			x		 = (x*stdevValue) + meanValue;
			
			again	 = ((x < lower) || (x > upper));
			
			count++;
			again	 = again && (count < maxtries);
		}
		
		return x;
	}
	
	public String toString() {
		String line = getClass().getSimpleName()+"\tmean="+meanValue+"\tdev="+stdevValue+"\tlower="+lower+"\tupper="+upper;
		return line;
	}

}
