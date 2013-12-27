package math;

/**
 * adapter class for MersenneTwisterFast random number generation
 * @author  Samantha Lycett
 * @created 23 Nov 2012
 * @version 17 June 2013 - add initialise from seed methods in here
 * @version 19 Oct  2013
 * @version 27 Dec  2013 - added getSeed method, useful for log file information
 */
public class Distributions {
	
	static long mySeed;
	
	public static long initialise() {
		mySeed = MersenneTwisterFast.getSeed();
		return mySeed;
	}
	
	public static void initialiseWithSeed(int seed) {
		MersenneTwisterFast.initialiseWithSeed(seed);
		mySeed = (long)seed;
	}
	
	public static long getSeed() {
		return mySeed;
	}

	/*
	public static double randomExponential(double mean) {
		double u = MersenneTwisterFast.getDouble();
		double x = (Math.log(1-u))*mean;
		return x;
	}
	*/
	
	public static double randomGaussian() {
		return MersenneTwisterFast.getGaussian();
	}
	
	public static double randomUniform() {
		return MersenneTwisterFast.getDouble();
	}
	
	public static int randomInt() {
		return MersenneTwisterFast.getInt();
	}
	
	public static int randomInt(int upper) {
		return MersenneTwisterFast.getInt(upper);
	}
	
	
	/**
	 * chooses index according to weights, weights are not cumulative or normalised
	 * @param weights
	 * @param totalWeights
	 * @return
	 */
	public static int chooseWithWeights(double[] weights, double totalWeights) {
		double x   = MersenneTwisterFast.getDouble() * totalWeights;
		double ctr = 0;
		int choice = -1;
		//int i      = 0;
		
		//while ( (i < weights.length) && (choice==-1) ) {
		for (int i = 0; i < weights.length; i++) {
			double ctr2 = ctr + weights[i];
			if ( (ctr < x) && (x < ctr2) ) {
				choice = i;
			}
			ctr = ctr2;
			//i++;
		}
		
		return choice;
	}
}
