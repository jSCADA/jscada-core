package ch.hevs.jscada.model;

/**
 * Interface in order to get notified about updates or value changes of a data point. The data point base class offers
 * the possibility to add one ore more listeners for either data point updates (whenever a new value is set) or for
 * data point changes (when a new value is set and the new value is different from the existing one.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 * @see ch.hevs.jscada.model.DataPoint
 */
public interface DataPointListener<T extends DataPoint> {
    /**
     * The data point dataPoint has been updated or changed (Depends which method used when adding the listener to the
     * data point.
     *
     * @param dataPoint The data point that has been updated or changed.
     */
    void dataPointUpdated(T dataPoint);
}
