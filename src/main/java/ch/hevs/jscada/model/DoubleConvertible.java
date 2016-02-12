package ch.hevs.jscada.model;

import ch.hevs.jscada.exception.ConversionException;

/**
 * All objects that can be converted from or to a double have to implement this interface.
 */
public interface DoubleConvertible {

	/**
	 * Returns the double representation of the object or the objet's value.
	 * 
	 * @return	Double representation of the object or the object's value.
	 */
	double getDoubleValue();

	/**
	 * Updates the object or the object's value using the given double.
	 * 
	 * @throws ConversionException 	Thrown if the value can not be converted from the given double.
	 * @param value 				Double representation of the object or object's value.
	 */
	void setDoubleValue(final double value) throws ConversionException;
}
