package ch.hevs.jscada.model;

import ch.hevs.jscada.exception.ConversionException;
import ch.hevs.jscada.exception.DuplicateIdException;

import java.lang.*;

/**
 * Implements a data point as integer number.
 */
public class IntegerDataPoint extends DataPoint {
	
	/**
	 * Creates the integer data point with the given ID. Note that the ID has to be unique and that the 
	 * point is automatically registered at the central registry.
	 * 
	 * @param id	ID of the data point. Can be any string as long it is unique.
	 * @param process	SCADA process to which the data point has to be added.
	 */
	public IntegerDataPoint(final String id, final Process process) throws DuplicateIdException {
		super(id, process);
	}
	
    @Override
	public DataPointType getType() {
		return DataPointType.INTEGER;
	}

	/**
     * Returns the data point's actual value.
     * 
     * @return	Data points integer value.
     */
    public long getValue() {
		return value;
	}
	
    /**
     * Updates the data point's value and notifies all listeners about the update.
     * 
     * @param value	The new value for the data point.
     */
	public void setValue(final long value) {
		boolean changed = value != this.value;
		this.value = value;
		update(changed);
	}
	
	@Override
	public String getStringValue() {
		return Long.toString(value);
	}

	@Override
	public void setStringValue(final String value) throws ConversionException {
		try { 
			setValue(Long.parseLong(value));
		} catch (NumberFormatException e) {
			throw new ConversionException(value, String.class, IntegerDataPoint.class);
		}
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}

	@Override
	public void setDoubleValue(final double value) throws ConversionException {
		setValue((long) value);
	}

	// Data point's value
    private long value;
}
