package ch.hevs.jscada.exception;

/**
 * This exception is thrown if either a mandatory configuration parameter is missing or invalid.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
@SuppressWarnings("serial")
public class ConfigurationException extends Exception {
	/**
	 * Creates a ConfigurationException with the given description message.
	 * 
	 * @param message	Exception description.
	 */
	public ConfigurationException(final String message) {
		super(message);
	}
}
