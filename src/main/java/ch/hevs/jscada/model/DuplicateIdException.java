package ch.hevs.jscada.model;

/**
 * This exception is thrown if an object in a set of uniquely identifiable objects has the same ID as the item to be 
 * to add to the set.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 * @see ch.hevs.jscada.model.DataPoint
 * @see ch.hevs.jscada.io.ConnectionGroup
 */
@SuppressWarnings("serial")
public class DuplicateIdException extends Exception {
	/**
	 * Creates a DuplicateIdException with the given description message.
	 * 
	 * @param id	Duplicate ID.
	 */
	public DuplicateIdException(String id) {
		super("A datapoint with the ID \"" + id + "\" exists already, ID have to be unique!");
	}
}
