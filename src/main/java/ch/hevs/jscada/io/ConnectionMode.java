package ch.hevs.jscada.io;

/**
 * Defines the synchronization mode of a connection. Based on the definition of the different field protocols and the 
 * abilities of the connection implementations, there are multiply scenarios possible how the actual values are read 
 * from or written to the target devices. Some protocols offer only support for polling the inputs and outputs, in this
 * case the mode would be SYCHRONEOUS, if the input have to be polled, but the change of an output can be triggered at
 * any time, the mode would be SYNCHRONEOUS_INPUTS. If the I/O is completely event based, the mode is EVENT_BASED.
 * 
 * Note that apart that a connection needs to support at least one of the modes, some connection implementations might 
 * support multiple modes.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public enum ConnectionMode {
	/**
	 * The connection is completely synchronous, for inputs as for outputs. This means that the inputs are only updated
	 * during the synchronize() method and the same applies for writing to the outputs.
	 */
	SYNCHRONEOUS,
	
	/**
	 * The connection is synchronous for inputs (inputs are only updated during the synchronize() method call), outputs
	 * however can change every time and the connection can transport the new values immediately to the target device.
	 */
	SYNCHRONEOUS_INPUTS,
	
	/**
	 * The connection is completely event based. This means that there is no need to call the synchronize() method on
	 * those connections. Input changes and output updates are spontaneous and can happen at any time.
	 */
	EVENT_BASED
}
