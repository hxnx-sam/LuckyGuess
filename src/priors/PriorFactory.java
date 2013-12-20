package priors;

/**
 * Factory class to generate priors
 * @author Samantha Lycett
 * @created 9 Dec 2013
 * @version 9 Dec 2013
 *
 */
public class PriorFactory {

	/**
	 * creates a prior from the priorName, using the default parameters for that prior
	 * @param priorName
	 * @return
	 */
	public static Prior createPrior(String priorName) {
		if (priorName.startsWith("Fixed")) {
			return new FixedValuePrior();
		} else if (priorName.startsWith("Normal")) {
			return new NormalPrior();
		} else if (priorName.startsWith("Uniform")) {
			return new UniformPrior();
		} else {
			System.out.println("Sorry PriorFactory is not configured to make "+priorName+" see createPrior method");
			return null;
		}
	}
	
	
}
