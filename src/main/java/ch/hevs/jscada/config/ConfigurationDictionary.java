package ch.hevs.jscada.config;

import ch.hevs.jscada.exception.ConfigurationException;

import java.util.Map;
import java.util.TreeMap;

/**
 * A ConfigurationDictionary is basically a dictionary of string keys and object values. It basically differs in the 
 * way the values are read from the dictionary: If a value is not present and no default value is given, a 
 * ConfigurationException is thrown in order to indicate that the value is missing. If there exists a value on the 
 * other hand but the type of the value does not match the requested type or the type of the default value, a
 * ConfigurationException is thrown too.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public class ConfigurationDictionary {
	/**
	 * Creates a ConfigurationDictionary using the given arguments in the following format --<key>=<value>. Note that 
	 * all value will be of type string. If a configuration value is not well formatted, it will be ignored.
	 * 
	 * @param args	The arguments to convert to a configuration dictionary.
	 */
	public ConfigurationDictionary(String... args) {
		for (String arg: args) {
			if (arg.startsWith("--") && arg.contains("=")) {
				String[] tokens = arg.split("=");
				if (tokens.length == 2) {
					set(tokens[0].replace("--", ""), tokens[1]);
				} 
			} 
		}
	}
	
	/**
	 * Returns the value for the given key if the value exists in the configuration or the default value if the
	 * configuration does not contain a value with the given key. Note that if a value is present in the configuration,
	 * it is checked that the type of the actual value in the configuration is compatible with the given default value.
	 *  
	 * @param key						Configuration option key.
	 * @param defaultValue				The default value to use in the absence of a value in the configuration.
	 * @return							The configured value or the default value in the absence of a value in the 
	 * 									configuration for the given key.
	 * @throws ConfigurationException	This exception is thrown if the type of the value in the configuration does not
	 * 									match the default value's type.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final String key, final T defaultValue) throws ConfigurationException {
		// Does a configuration parameter with the given key exists?
		if (properties.containsKey(key)) {
			// Get the value as Java basic object.
			final Object value = properties.get(key);
			final Class<?> type = defaultValue.getClass();
			
			// If the types are compatible, just return the casted value.
			if (type.isAssignableFrom(value.getClass())) {
				return (T)value;
				
			// Otherwise, in the case the value in the configuration is of type string, try to parse the value...
			} else if (String.class.isAssignableFrom(value.getClass())) {
				Object out;
				if (defaultValue.getClass().isAssignableFrom(Boolean.class)) {
					out = Boolean.parseBoolean((String)value);
				} else if (defaultValue.getClass().isAssignableFrom(Integer.class)) {
					out = Integer.parseInt((String)value);
				} else if (defaultValue.getClass().isAssignableFrom(Double.class)) {
					out = Double.parseDouble((String)value);
					
				// If there is now conversion known, fail.
				} else {
					throw new ConfigurationException("Impossible to convert parameter \"" + key + "\" (value=" + 
							 value.toString() + ") to type " + defaultValue.getClass().getName());
				}
				
				// If the conversion was successful, return the value
				return (T)out;
				
			// All other types can not be processed.
			} else {
				throw new ConfigurationException("Impossible to convert parameter \"" + key + "\" (value=" + 
						 value.toString() + ") to type " + type.getName());
			}
			
		// If there is no value for the given key in the configuration, return the default value.
		} else {
			return defaultValue;
		}
	}
	
	/**
	 * Returns the value for the given key if the value exists in the configuration or fails with an 
	 * ConfigurationException if the configuration does not contain a value with the given key. Note that the method 
	 * can fail even if a value is present for the given key in the configuration if the type of the actual value in 
	 * the configuration is not compatible with the requested type.
	 * 
	 * @param key						Configuration option key.
	 * @param valueClass				Class of which the configuration value has to be.
	 * @return							The configured value.
	 * @throws ConfigurationException	This exception is thrown if the configuration parameter is missing or if the 
	 * 									type of the value in the configuration does not match the default value's type.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final String key, final Class<T> valueClass) throws ConfigurationException {
		// Does a configuration parameter with the given key exists?
		if (properties.containsKey(key)) {
			// Get the value as Java basic object.
			final Object value = properties.get(key);
			
			// If the types are compatible, just return the casted value.
			if (valueClass.isAssignableFrom(value.getClass())) {
				return (T)value;
				
			// Otherwise, in the case the value in the configuration is of type string, try to parse the value...	
			} else if (String.class.isAssignableFrom(value.getClass())) {
				Object out;
				if (valueClass.isAssignableFrom(Boolean.class)) {
					out = Boolean.parseBoolean((String)value);
				} else if (valueClass.isAssignableFrom(Integer.class)) {
					out = Integer.parseInt((String)value);
				} else if (valueClass.isAssignableFrom(Double.class)) {
					out = Double.parseDouble((String)value);
					
				// If there is now conversion known, fail.
				} else {
					throw new ConfigurationException("Impossible to convert parameter \"" + key + "\" (value=" + 
							 value.toString() + ") to type " + valueClass.getName());
				}
				
				// If the conversion was successful, return the value.
				return (T)out;
				
			// All other types can not be processed.
			} else {
				throw new ConfigurationException("Impossible to convert parameter \"" + key + "\" (value=" + 
												 value.toString() + ") to type " + valueClass.getName());
			}
			
		// If the value is missing, fail with an ConfigurationException.
		} else {
			throw new ConfigurationException("Missing mandatory configuration parameter \"" + key + "\"");
		}
	}
	
	/**
	 * Returns true if the configuration dictionary contains the given key, false if not.
	 * 
	 * @param key	Key to check.
	 * @return		True if the configuration dictionary contains the key, false otherwise.
	 */
	public boolean contains(String key) {
		return properties.containsKey(key);
	}
	
	/**
	 * Sets the configuration with the given key to the given value. Note that existing value will be overridden.
	 * 
	 * @param key		Key for the configuration value to set.
	 * @param value		The actual configuration value.
	 */
	public <T> void set(final String key, final T value) {
		properties.put(key, value);
	}
	
	private final Map<String,Object> properties = new TreeMap<>();
}
