package ch.hevs.jscada.io;

/**
 * This exception is thrown when an error happens during the initialization of a SCADA connection.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
@SuppressWarnings("serial")
public class ConnectionInitializeException extends Exception {
	/**
	 * Creates a ConnectionInitializeException with the given description message.
	 * 
	 * @param message	Exception description.
	 */
	public ConnectionInitializeException(final String message) {
		super(message);
	}
	
	/**
	 * Creates a ConnectionInitializeException with the given exception that caused the initialization to fail.
	 * 
	 * @param causingException	Exception causing the initialization to fail.
	 */
	public ConnectionInitializeException(final Exception causingException) {
		super("FieldConnection caused exception during initialisation: " + causingException.getMessage());
	} 
}
