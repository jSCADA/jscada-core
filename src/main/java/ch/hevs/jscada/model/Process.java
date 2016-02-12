package ch.hevs.jscada.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.hevs.jscada.exception.DuplicateIdException;

/**
 * The SCADA process contains all elements defined by a certain SCADA installation or system. It organizes all
 * data points, alarms, events and other data related to a SCADA model.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public class Process {
    /**
     * Returns the data point with the given ID if it exists, otherwise it returns null.
     * 
     * @param id	ID of the data point to find.
     * @return		Reference to the data point or null if the point does not exist.
     */
    public DataPoint getDataPoint(final String id) {
        return dataPoints.get(id);
    }

    /**
     * Returns the data point with the given ID and given type if it exists and is of the given type, 
     * otherwise the method returns null.
     * 
     * @param id				ID of the data point to find.
     * @param dataPointClass	Class of which the data point has to be.
     * @return					Reference to the data point or null if the point does not exist or is of another type.
     */
    @SuppressWarnings("unchecked")
	public <T extends DataPoint> T getDataPoint(final String id, final Class<T> dataPointClass) {
    	final DataPoint dataPoint = getDataPoint(id);
    	if (dataPoint != null && dataPointClass != null && dataPointClass == dataPoint.getClass()) {
    		return (T)dataPoint;
    	} else {
    		return null;
    	}
    }
    
    /**
     * Returns the complete list of all data points.
     * 
     * @return	List of all data points registered within the system.
     */
    public Collection<DataPoint> getDataPoints() {
        return dataPoints.values();
    }
    
    /**
     * Removes the given listener from all data points.
	 * 
	 * @param listener	Reference to the listener.
     */
    public void removeListenerFromAllDataPoints(final DataPointListener listener) {
        for (final DataPoint dataPoint : dataPoints.values()) {
            dataPoint.removeListener(listener);
        }
    }
    
	void addDataPoint(final String id, final DataPoint dataPoint) throws DuplicateIdException {
		// Check for duplicate ID.
    	if (dataPoints.containsKey(id)) {
    		throw new DuplicateIdException(id);
    	}
    	
    	// Register the data point.
        dataPoints.put(id, dataPoint);
	}
	
	// Global data points registry.
    private final Map<String,DataPoint> dataPoints = new HashMap<>();
}
