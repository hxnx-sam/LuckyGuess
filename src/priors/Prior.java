package priors;

import abc.Parameter;

/**
 * Interface for Prior classes
 * @author Samantha Lycett
 * @created 19 Oct 2013
 * @version 19 Oct 2013
 *
 */
public interface Prior {

	public boolean	allowSet(Parameter p);
	public void 	setParameter(Parameter p);
	public Double 	draw();
	
}
