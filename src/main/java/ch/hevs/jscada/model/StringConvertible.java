package ch.hevs.jscada.model;

import ch.hevs.jscada.exception.ConversionException;

/**
 * All objects that can be converted from or to a string have to implement this interface.
 */
public interface StringConvertible {

	/**
	 * Returns the string representation of the object or the objet's value.
	 * 
	 * @return	String representation of the object or the object's value.
	 */
	String getStringValue();

	/**
	 * Updates the object or the object's value using the given string.
	 * 
	 * @throws ConversionException 	Thrown if the value can not be converted from the given string.
	 * @param value 				String representation of the object or object's value.
	 */
	 void setStringValue(final String value) throws ConversionException;
}
