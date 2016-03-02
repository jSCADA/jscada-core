package ch.hevs.jscada.model;

/**
 * All objects that can be converted from or to a double have to implement this interface.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public interface DoubleConvertible {

    /**
     * Returns the double representation of the object or the object's value.
     *
     * @return Double representation of the object or the object's value.
     */
    double getDoubleValue();

    /**
     * Updates the object or the object's value using the given double.
     *
     * @param value Double representation of the object or object's value.
     * @param owner Reference to the object that wants to write the value. Used to check if the object has selected the
     *              datapoint before.
     * @throws ConversionException Thrown if the value can not be converted from the given double.
     */
    void setDoubleValue(double value, Object owner) throws ConversionException, SelectException;
}
