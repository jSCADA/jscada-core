package ch.hevs.jscada.model;

/**
 * Type of a data point.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 * @see ch.hevs.jscada.model.DataPoint
 */
public enum DataPointType {
	/**
	 * The data point is of an invalid type.
	 */
	INVALID, 
	
	/**
	 * The data point holds a boolean value.
	 */
	BOOLEAN, 
	
	/**
	 * The data point holds a integer value.
	 */
	INTEGER, 
	
	/**
	 * The data point holds a floating point number value.
	 */
	FLOATING_POINT;
	
	/**
	 * Returns the type enumerator matching the given string. Note that this method is more tolerant than the build-in
	 * method valueOf() as it accepts lower case string and abbreviations.
	 * 
	 * @param string	String representing the data point type.
	 * @return			The data point type that corresponds to the string or INVALID if the string does not correspond
	 * 					to a valid data point name. 
	 */
	public static DataPointType fromString(final String string) {
		if (string != null) {
			String lower = string.toLowerCase();
			switch (lower) {
				case "boolean":
					return BOOLEAN;
				case "int":
				case "integer":
					return INTEGER;
				case "float":
				case "floating_point":
					return FLOATING_POINT;
				default:
					return INVALID;
			}
		} else {
			return INVALID;
		}
	}
}
