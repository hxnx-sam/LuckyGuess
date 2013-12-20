package abc;

/**
 * class to represent a ResultStatistic
 * @author Samantha Lycett
 * @created 19 Oct 2013
 * @version 20 Oct 2013
 *
 */
public class ResultStatistic implements Statistic {
	
	String name	 = null;
	Double value = 0.0;
	Double tol	 = 1e-8;
	
	public ResultStatistic() {
		
	}
	
	/**
	 * constructor for ResultStatistic, specifying name and value, and using default tolerance = 1e-8
	 * @param name
	 * @param value
	 */
	public ResultStatistic(String name, Double value) {
		setName(name);
		setValue(value);
	}
	
	public ResultStatistic(String name, Double value, Double tol) {
		setName(name);
		setValue(value);
		setTolerance(tol);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}
	
	public void setToleranceAsFraction(double fraction) {
		this.tol = value*fraction;
	}

	@Override
	public void setTolerance(Double tol) {
		this.tol = tol;
	}

	@Override
	public Double getTolerance() {
		return tol;
	}

	/**
	 * returns true if the other value is close to this value, within this tolerance
	 * does not check names
	 */
	@Override
	public boolean closeTo(Statistic other) {
		
		boolean near = ( Math.abs( value - other.getValue() ) <= tol );
		return near;
	}
	
	/**
	 * returns the absolute difference between this value and the other value
	 * does not check names
	 */
	@Override
	public double distanceFrom(Statistic other) {
		
		double dist = Math.abs( value - other.getValue() );
		return dist;
		
	}
	
	///////////////////////////////////////////////////////////////////////////////////

	public String toString() {
		return (name + ":" + value+"\ttol:"+tol);
	}

	/**
	 * hashCode based on the name of this ResultStatistic only
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * returns true if this ResultStatistic has the same name as the other ResultStatistic object
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (obj instanceof ResultStatistic) {
			ResultStatistic other = (ResultStatistic) obj;
			if (name == null) {
				if (other.name != null) {
					return false;
				} else {
					return true;
				}
			} else {
				if (name.equals(other.name)) {
					return true;
				} else {
					return false;
				}
			}
		} else if (obj instanceof Statistic) {
			Statistic other = (Statistic)obj;
			if (name == null) {
				if (other.getName() != null) {
					return false;
				} else {
					return true;
				}
			} else {
				if (name.equals(other.getName() )) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
	
	
}
