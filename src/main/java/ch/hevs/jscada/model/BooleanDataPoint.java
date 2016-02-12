package ch.hevs.jscada.model;

import ch.hevs.jscada.exception.ConversionException;
import ch.hevs.jscada.exception.DuplicateIdException;

import java.lang.*;

/**
 * Implements a boolean data point.
 */
public class BooleanDataPoint extends DataPoint {
	
	/**
	 * Creates the boolean data point with the given ID. Note that the ID has to be unique and that the 
	 * point is automatically registered at the central registry.
	 * 
	 * @param id		ID of the data point. Can be any string as long it is unique.
	 * @param process	SCADA process to which the data point has to be added.
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
     * @return	Data points boolean value.
     */
    public boolean getValue() {
		return value;
	}
	
    /**
     * Updates the data point's value and notifies all listeners about the update.
     * 
     * @param value	The new value for the data point.
     */
	public void setValue(final boolean value) {
		boolean changed = value != this.value;
		this.value = value;
		update(changed);
	}
	
	@Override
	public String getStringValue() {
		return Boolean.toString(value);
	}

	@Override
	public void setStringValue(final String value) throws ConversionException {
		try {
			setValue(Boolean.parseBoolean(value));
		} catch (NumberFormatException e) {
			throw new ConversionException(value, String.class, BooleanDataPoint.class);
		}
	}
	
	@Override
	public double getDoubleValue() {
		return value ? 1 : 0;
	}

	@Override
	public void setDoubleValue(final double value) throws ConversionException {
		setValue(value != 0);
	}

	// Data point's value.
    private boolean value = false;
}
