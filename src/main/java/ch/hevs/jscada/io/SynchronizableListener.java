package ch.hevs.jscada.io;

/**
 * A SynchronizableListener can be registered within a Synchronizable in order to get notified just before and just
 * after the actual synchronization takes/took place. Note that such a listener can be registered within any class
 * instance where the class implements the Synchronizable interface. In addition to the Connection class, the 
 * ConnectionGroup class implements the interface as well.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 * @see ch.hevs.jscada.io.Synchronizable
 * @see ch.hevs.jscada.io.Connection
 * @see ch.hevs.jscada.io.ConnectionGroup
 */
public interface SynchronizableListener {
	/**
	 * This method will be called by the Synchronizable just before synchronizing.
	 * 
	 * @param synchronizable	The Synchronizable that will be synchronized.
	 */
	void willSynchronize(Synchronizable synchronizable);
	
	/**
	 * This method will be called by the Synchronizable just after synchronizing took place.
	 * 
	 * @param synchronizable	The Synchronizable that was synchronized.
	 */
	void didSynchronize(Synchronizable synchronizable);
}
