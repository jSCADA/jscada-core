package ch.hevs.jscada.model;

/**
 * An object implementing this interface can be registered within a {@link Process} in order to get notified about the
 * addition of new data points.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public interface ProcessListener {
    /**
     * A datapoint has been added to the process to which the listener was added before.
     *
     * @param process   The process which has just added the new datapoint.
     * @param dataPoint The data point that was just added to the process.
     */
    void dataPointAdded(Process process, DataPoint dataPoint);
}
