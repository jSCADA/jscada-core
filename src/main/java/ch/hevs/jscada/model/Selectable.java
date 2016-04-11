package ch.hevs.jscada.model;

/**
 * Interface for all selectable objects. An object can be selected in order to give exclusive rights to modify the
 * object to the object that actually selected the object. This principle is known as select before operate and is
 * adapted in many industrial standards.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public interface Selectable {

    /**
     * Tries to select the object with the given object as the owner. In order to avoid racing conditions between two
     * entities by modifying the same resource at once, each entity should first select the object (eg. acquire the
     * right to modify the resource) and only if this selection was successful, the object can be modified, otherwise
     * not.
     *
     * @param object Object holding the modifying ownership for the select.
     */
    void select(Object object) throws SelectException;

    /**
     * Returns true if the Selectable is already selected, false otherwise.
     *
     * @return  True if already selected, false otherwise.
     */
    boolean isSelected();

    /**
     * Deselects the data point after a write operation on the data point in order to enable other entities write
     * access too.
     *
     * @param object Object actually holding the write ownership.
     */
    void deselect(Object object);
}
