package math;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author sam
 * @version 19 Oct 2013
 */

public class DistributionsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("** Distributions Junit Test **");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("** END **");
	}

	@Before
	public void setUp() throws Exception {
		//MersenneTwisterFast.initialiseWithSeed(12345);
	}

	@After
	public void tearDown() throws Exception {
	}

		
	@Test
	public void testChooseWithWeights() {
		System.out.println("- testChooseWithWeights -");
		System.out.println("- with weight to any value - ");
		
		//double[] weights 	= {1, 2, 3, 4};	
		double[] weights 	= {1, 2, 3, 4};	
		double totalWeight  = 0;
		for (int i = 0; i < weights.length; i++) {
			System.out.println(i+"\tweight="+weights[i]);
			totalWeight += weights[i];
		}
		System.out.println("Total Weight = "+totalWeight);
		
		int nreps 			= 10000;
		int[] vals			= new int[weights.length];
		for (int i = 0; i < vals.length; i++) {
			vals[i]			= 0;
		}
		
		for (int r = 0; r < nreps; r++) {
			int choice 	 = Distributions.chooseWithWeights(weights, totalWeight);
			vals[choice] = vals[choice]+1;
		}
		
		System.out.println("Number of reps = "+nreps);
		for (int i = 0; i < vals.length; i++) {
			System.out.println(i+"\ttimes chosen = "+vals[i]);
		}
		
		System.out.println("---------------");
		
		
	}

}
