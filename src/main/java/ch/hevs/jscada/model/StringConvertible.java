package ch.hevs.jscada.model;

/**
 * All objects that can be converted from or to a string have to implement this interface.
 */
public interface StringConvertible {

    /**
     * Returns the string representation of the object or the object's value.
     *
     * @return String representation of the object or the object's value.
     */
    String getStringValue();

    /**
     * Updates the object or the object's value using the given string.
     *
     * @param value String representation of the object or object's value.
     * @param owner Reference to the object that wants to write the value. Used to check if the object has selected the
     *              datapoint before.
     * @throws ConversionException Thrown if the value can not be converted from the given string.
     */
    void setStringValue(String value, Object owner) throws ConversionException, SelectException;
}
