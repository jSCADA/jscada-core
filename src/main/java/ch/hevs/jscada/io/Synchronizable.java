package ch.hevs.jscada.io;

/**
 * Interface for all classes which can be synchronized. Synchronizing means that the inputs and outputs are read/written
 * only upon action from outside. Modbus is an example protocol which has to be synchronized in order to get new 
 * values from the field.
 *  
 * @author Michael Clausen (michael.clausen@hevs.ch)
 * @see SynchronizableListener
 */
public interface Synchronizable {
	/**
	 * Synchronizes the object - Reads and writes inputs/outputs.
	 */
	void synchronize();
	
	/**
	 * Adds the given synchronization listener. The listener will be informed just before and just after the 
	 * synchronization takes/took place.
	 * 
	 * @param listener	Reference to the listener to add.
	 */
	void addListener(SynchronizableListener listener);
	
	/**
	 * Removes the given synchronization listener.
	 * 
	 * @param listener	Reference to the listener to remove.
	 */
	void removeListener(SynchronizableListener listener);
}
