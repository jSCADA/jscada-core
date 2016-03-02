package ch.hevs.jscada.model;

/**
 * This exception is thrown if a data point could not be converted to the requested value. This is mostly the case when
 * using the StringConvertible interface, as not all string values can be converted to native values without errors.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 * @see ch.hevs.jscada.model.DoubleConvertible
 * @see ch.hevs.jscada.model.StringConvertible
 */
@SuppressWarnings("serial")
public class ConversionException extends Exception {
    /**
     * Creates a ConversionException with the given description message.
     *
     * @param value Value as string.
     * @param from  Source type.
     * @param to    Target type.
     */
    public ConversionException(final String value, final Class<?> from, final Class<?> to) {
        super("Can not convert value \"" + value + "\" from " + from.getName() + " to " + to.getName());
    }
}
