package ch.hevs.jscada.model;

import java.util.Locale;

/**
 * Implements a boolean data point.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public class BooleanDataPoint extends DataPoint {
    // Data point's value.
    private boolean value = false;

    /**
     * Creates the boolean data point with the given ID. Note that the ID has to be unique and that the
     * point is automatically registered at the central registry.
     *
     * @param id      ID of the data point. Can be any string as long it is unique.
     * @param process SCADA process to which the data point has to be added.
     */
    public BooleanDataPoint(final String id, final Process process) throws DuplicateIdException {
        super(id, process);
    }

    @Override
    public DataPointType getType() {
        return DataPointType.BOOLEAN;
    }

    /**
     * Returns the data point's actual value.
     *
     * @return Data points boolean value.
     */
    public boolean getValue() {
        return value;
    }

    /**
     * Updates the data point's value and notifies all listeners about the update.
     *
     * @param value The new value for the data point.
     * @param owner Reference to the object that wants to write the value. Used to check if the object has selected the
     *              datapoint before.
     */
    public void setValue(final boolean value, final Object owner) throws SelectException {
        ensureSelectedBy(owner);
        boolean changed = value != this.value;
        this.value = value;
        update(changed);
    }

    @Override
    public String getStringValue() {
        return Boolean.toString(value);
    }

    @Override
    public void setStringValue(final String value, final Object owner) throws ConversionException, SelectException {
        if (value != null) {
            if (value.toLowerCase(Locale.ROOT).equals("true") || value.toLowerCase(Locale.ROOT).equals("false")) {
                setValue(Boolean.parseBoolean(value), owner);
            } else {
                throw new ConversionException(value, String.class, BooleanDataPoint.class);
            }
        } else {
            throw new ConversionException(null, String.class, BooleanDataPoint.class);
        }
    }

    @Override
    public double getDoubleValue() {
        return value ? 1 : 0;
    }

    @Override
    public void setDoubleValue(final double value, final Object owner) throws ConversionException, SelectException {
        setValue(value != 0, owner);
    }
}
