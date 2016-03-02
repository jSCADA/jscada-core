package ch.hevs.jscada.model;

/**
 * Type of a data point.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 * @see ch.hevs.jscada.model.DataPoint
 */
public enum DataPointType {
    /**
     * The data point is of an invalid type.
     */
    INVALID,

    /**
     * The data point holds a boolean value.
     */
    BOOLEAN,

    /**
     * The data point holds a integer value.
     */
    INTEGER,

    /**
     * The data point holds a floating point number value.
     */
    FLOATING_POINT
}
