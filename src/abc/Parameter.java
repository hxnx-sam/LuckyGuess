package abc;

/**
 * Parameter class for Model
 * @author Samantha Lycett
 * @created 20 Oct 2013
 * @version 20 Oct 2013
 * @version 6  Dec 2013
 * @version 11 Dec 2013
 *
 */
public class Parameter {
	
	String name;
	double value;
	
	public Parameter() {
		
	}
	
	public Parameter(String name, double value) {
		this.name  = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public String toString() {
		return (name + ":" + value);
	}

	///////////////////////////////////////////////////////
	// hash code and equals based on name only
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Parameter other = (Parameter) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	*/

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if  ( obj instanceof TextParameter) {
			String otherName = ((TextParameter)obj).getName();
			return (name.equals(otherName));
		} else if  ( obj instanceof Parameter) {
			String otherName = ((Parameter)obj).getName();
			return (name.equals(otherName));
		} else if  ( obj instanceof String) {
			String otherName = (String)obj;
			return (name.equals(otherName));
		} else {
			return false;
		}
		
		/*
		if (getClass() != obj.getClass()) {
			return false;
		}
		TextParameter other = (TextParameter) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
		*/
	}
	
}
