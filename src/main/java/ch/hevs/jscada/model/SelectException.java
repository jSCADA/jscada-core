package ch.hevs.jscada.model;

/**
 * This exception is thrown if either an object tries to select a data point in order to set the data point's value and
 * the data point is already selected by another object or if the object tries to set the data point's value, but the
 * data point was not selected before by the object.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public class SelectException extends Exception {
    public SelectException(final String message) {
        super(message);
    }
}
