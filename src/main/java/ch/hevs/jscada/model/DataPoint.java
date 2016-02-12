package ch.hevs.jscada.model;

import ch.hevs.jscada.exception.ConversionException;
import ch.hevs.jscada.exception.DuplicateIdException;

import java.util.LinkedList;
import java.util.List;

/**
 * The base class of each data point in the SCADA process model. 
 * 
 * Additionally this base class disposes the central repository where every data point is registered. There is no need 
 * to register any data point manually, this is done automatically in the constructor for you.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public abstract class DataPoint implements Selectable, StringConvertible, DoubleConvertible, Comparable<DataPoint> {
	
	/**
	 * Constructor, creates a data point with the given ID and registers the data point within the global data point
	 * registry.
	 * 
	 * @param id		ID of the data point. Note that the ID should be unique across a system
	 * @param process	SCADA process to which the data point has to be added.
	 * @throws DuplicateIdException
	 */
    protected DataPoint(final String id, final Process process) throws DuplicateIdException {
    	// Register data point within the SCADA system.
    	if (process != null) {
    		process.addDataPoint(id, this);
    	}
    	
    	// Save ID.
        this.id = id;
    }
    
    /**
     * Returns the actual type of the data point.
     * 
     * @return	Data point type.
     */
    public abstract DataPointType getType();
    
    /**
     * Returns the ID of the data point. The ID can be any string, but it has to be unique.
     * 
     * @return	Data point's ID.
     */
    public final String getId() {
        return id;
    }

	/**
	 * Adds a listener to the data point. If onlyOnchanges is false, the listener will be informed about each update 
	 * of the data point. Update means that the value has been updated, but not necessarily that the value has changed.
	 * If onlyOnChanges is true, the data listener is informed only if the value actually changes.
	 * 
	 * @param listener		Reference to the listener.
	 * @param onlyOnChanges	If true, the listener is informed only if the data point's value effectively changes.
	 */
    public final void addListener(final DataPointListener listener, final boolean onlyOnChanges) {
    	if (listener != null) {
    		// Depending if the listener is added for updates or changes, add it to the respective list.
    		if (onlyOnChanges) {
    			changeListeners.add(listener);
    		} else {
    			updateListeners.add(listener);
    		}
    	}		
    }

    /**
	 * Removes the given listener from the list of listeners for the data point.
	 * 
	 * @param listener	Reference to the listener.
	 */
    public final void removeListener(final DataPointListener listener) {
    	if (listener != null) {
    		// Remove the listener from booth lists.
    		changeListeners.remove(listener);
    		updateListeners.remove(listener);
    	}
    }
    
    /**
	 * Tries to select the data point with the given object as the owner. In order to avoid racing conditions between 
	 * two entities by writing to the same data point at once, each entity should first select the object (eg. acquire 
	 * the right to write into the data point) and only if this has worked, the value of the data point can be changed.
	 * 
	 * @param object	Object holding the write ownership for the select.
	 * @return			True if the data point could be selected, false otherwise (already selected by another entity).
	 */
	@Override
	public final boolean select(final Object object) {
		if (object != null && selectOwner == null) {
			selectOwner = object;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Deselects the data point after a write operation on the data point in order to enable other entities write 
	 * access too.
	 * 
	 * @param object	Object actually holding the write ownership.
	 */
	@Override
	public final void deselect(final Object object) {
		if (object == selectOwner) {
			selectOwner = null;
		}
	}
	
	/**
	 * Each data point has to be able to convert his actual value into a string.
	 * 
	 * @return String representation of the data point.
	 */
	@Override
	abstract public String getStringValue();
	
	/**
	 * Each data point has to be able to convert a given string into his new value.
	 * 
	 * @param value String representation of the data point.
	 */
	@Override
	abstract public void setStringValue(final String value) throws ConversionException;

	/**
	 * Each data point has to be able to convert his actual value into a double.
	 * 
	 * @return Double representation of the data point.
	 */
	@Override
	abstract public double getDoubleValue();
	
	/**
	 * Each data point has to be able to convert a given double into his new value.
	 * 
	 * @param value Double representation of the data point.
	 */
	@Override
	abstract public void setDoubleValue(final double value) throws ConversionException;   
	
	/**
	 * Informs all listeners about the change of the data point's value.
	 * 
	 * @param changed	Set to true to indicate that the value has not only updated, it has changed too.
	 */
    protected final void update(final boolean changed) {
    	for (final DataPointListener listener: updateListeners) {
    		listener.dataPointUpdated(this);
    	}
    	if (changed) {
    		for (final DataPointListener listener: changeListeners) {
    			listener.dataPointUpdated(this);
    		}
    	}
    }
    
    @Override
	public int compareTo(DataPoint other) {
    	if (other != null) {
    		return id.compareTo(other.id);
    	} else {
    		return 1;
    	}
	}

	// Data point identification.
	private final String id;	

    // Data point listeners.
	private final List<DataPointListener> updateListeners = new LinkedList<>();
	private final List<DataPointListener> changeListeners = new LinkedList<>();

    // Operate locking using select before operate principle.
    private Object selectOwner = null;
}
