package ch.hevs.jscada.config;

import ch.hevs.jscada.exception.ConfigurationException;

import java.util.Map;
import java.util.TreeMap;

/**
 * A {@link ConfigurationDictionary} is basically a dictionary or a map of {@link String} based keys and {@link Object}
 * based values.
 * <br><br>
 * It basically differs from them in the way the values are read from the configuration dictionary:<br>
 * If a value is not present in the configuration dictionary and no default value is provided, a
 * {@link ConfigurationException} is thrown in order to indicate that the configuration value is missing. If there
 * exists a value but the type of the value does not match the requested type or the type of the provided default value,
 * a {@link ConfigurationException} is thrown too.
 * <br><br>
 * This basically simplifies a jSCADA developers work as he has only to use this interface in order get the
 * configuration data and all error reporting related to invalid type or value is already handled by the framework.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public class ConfigurationDictionary {
    private final Map<String, Object> properties = new TreeMap<>();

    /**
     * Creates a {@link ConfigurationDictionary} using the given arguments in the following format
     * <b>--{key}={value}</b>.
     * <br><br>
     * Note that all value types will be of type {@link String}. If a configuration value is not well formatted, it will
     * be silently ignored.
     * <br><br>
     * This constructor enables a simple mechanism to convert command line parameters into a configuration dictionary
     * that can be used almost anywhere inside jSCADA.
     *
     * @param arguments The arguments to convert to a configuration dictionary.
     */
    public ConfigurationDictionary(final String... arguments) {
        for (final String argument : arguments) {
            if (argument.startsWith("--") && argument.contains("=")) {
                final String[] tokens = argument.split("=");
                if (tokens.length == 2) {
                    set(tokens[0].replace("--", ""), tokens[1]);
                }
            }
        }
    }

    /**
     * Returns the value for the given key if the value exists in the configuration dictionary or returns the default
     * value if the configuration dictionary does not contain a value with the given key. Note that if a value is
     * present in the configuration, it is checked that the type of the actual value in the configuration can be
     * converted to the requested data type and if that is not the case, an exception is thrown.
     *
     * @param <T>          The type of the configuration parameter.
     * @param key          Configuration option key.
     * @param defaultValue The default value to use in the absence of a value in the configuration.
     * @return The configured value or the default value in the absence of a value in the configuration dictionary for
     * the given key.
     * @throws ConfigurationException This exception is thrown if the type of the value in the configuration does not
     *                                match the default value's type or the value can not be converted to the same type.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final String key, final T defaultValue) throws ConfigurationException {
        // Does a configuration parameter with the given key exists?
        if (properties.containsKey(key)) {
            // Get the value as Java Object.
            final Object value = properties.get(key);
            final Class<?> type = defaultValue.getClass();

            // If the types are compatible, just cast the value and return it.
            if (type.isAssignableFrom(value.getClass())) {
                return (T) value;
            } else if (String.class.isAssignableFrom(value.getClass())) {
                // Otherwise, in the case the value in the configuration is of type string, try to parse the value...
                if (defaultValue.getClass().isAssignableFrom(Boolean.class)) {
                    return (T) Boolean.valueOf(Boolean.parseBoolean((String) value));
                } else if (defaultValue.getClass().isAssignableFrom(Long.class)) {
                    return (T) Long.valueOf(Long.parseLong((String) value));
                } else if (defaultValue.getClass().isAssignableFrom(Double.class)) {
                    return (T) Double.valueOf(Double.parseDouble((String) value));
                } else {
                    // If there is now conversion known, fail.
                    throw new ConfigurationException("Impossible to convert parameter \"" + key + "\" (value=" +
                        value.toString() + ") to type " + defaultValue.getClass().getName());
                }
            } else {
                // All other types can not be converted at all.
                throw new ConfigurationException("Impossible to convert parameter \"" + key + "\" (value=" +
                    value.toString() + ") to type " + type.getName());
            }
        } else {
            // If there is no value for the given key in the configuration, return the default value.
            return defaultValue;
        }
    }

    /**
     * Returns the value for the given key if the value exists in the configuration or fails with an
     * ConfigurationException if the configuration does not contain a value with the given key. Note that the method
     * can fail even if a value is present for the given key in the configuration if the type of the actual value in
     * the configuration is not compatible with the requested type.
     *
     * @param <T>        The type of the configuration parameter.
     * @param key        Configuration option key.
     * @param valueClass Class of which the configuration value has to be.
     * @return The configured value.
     * @throws ConfigurationException This exception is thrown if the configuration parameter is missing or if the
     *                                type of the value in the configuration does not match the default value's type.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final String key, final Class<T> valueClass) throws ConfigurationException {
        // Does a configuration parameter with the given key exists?
        if (properties.containsKey(key)) {
            // Get the value as Java Object.
            final Object value = properties.get(key);

            // If the types are compatible, just return the casted value.
            if (valueClass.isAssignableFrom(value.getClass())) {
                return (T) value;
            } else if (String.class.isAssignableFrom(value.getClass())) {
                // Otherwise, in the case the value in the configuration is of type string, try to parse the value...
                if (valueClass.isAssignableFrom(Boolean.class)) {
                    return (T) Boolean.valueOf(Boolean.parseBoolean((String) value));
                } else if (valueClass.isAssignableFrom(Integer.class)) {
                    return (T) Long.valueOf(Long.parseLong((String) value));
                } else if (valueClass.isAssignableFrom(Double.class)) {
                    return (T) Double.valueOf(Double.parseDouble((String) value));
                } else {
                    // If there is now conversion known, fail.
                    throw new ConfigurationException("Impossible to convert parameter \"" + key + "\" (value=" +
                        value.toString() + ") to type " + valueClass.getName());
                }
            } else {
                // All other types can not be processed.
                throw new ConfigurationException("Impossible to convert parameter \"" + key + "\" (value=" +
                    value.toString() + ") to type " + valueClass.getName());
            }

            // If the value is missing, fail with an ConfigurationException.
        } else {
            throw new ConfigurationException("Missing mandatory configuration parameter \"" + key + "\"");
        }
    }

    /**
     * Returns true if the configuration dictionary contains a value for the given key, false if not.
     *
     * @param key Key to check.
     * @return True if the configuration dictionary contains a value for the key, false otherwise.
     */
    public boolean contains(String key) {
        return properties.containsKey(key);
    }

    /**
     * Sets the configuration with the given key to the given value. Note that existing value will be silently
     * overwritten.
     *
     * @param <T>   The type of the configuration parameter.
     * @param key   Key for the configuration value to set.
     * @param value The actual configuration value.
     */
    public <T> void set(final String key, final T value) {
        properties.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        for (Map.Entry entry : properties.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
        }
        builder.append("\n}");
        return builder.toString();
    }
}
