package abc;

/**
 * interface for Model output results - these are usually statistics
 * @author Samantha Lycett
 * @created 19 Oct 2013
 * @version 20 Oct 2013
 *
 */
public interface Statistic {

	public void 	setName(String name);
	public String 	getName();
	
	public void 	setValue(Double value);
	public Double	getValue();
	
	public void		setTolerance(Double tol);
	public Double	getTolerance();
	
	public boolean	closeTo(Statistic other);
	public double	distanceFrom(Statistic other);
}
