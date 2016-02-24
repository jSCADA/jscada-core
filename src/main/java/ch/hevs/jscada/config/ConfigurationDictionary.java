package ch.hevs.jscada.config;

import ch.hevs.jscada.exception.ConfigurationException;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
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

    private static class MissingConfigurationParameterException extends ConfigurationException {
        public MissingConfigurationParameterException(final String key) {
            super("Missing mandatory configuration parameter \"" + key + "\"");
        }
    }

    private static class InvalidConfigurationParameterTypeException extends ConfigurationException {
        public InvalidConfigurationParameterTypeException(final String key, Object value, Class target) {
            super("Impossible to convert parameter \"" + key + "\" (value=" +
                String.valueOf(value) + ") of " + String.valueOf(value.getClass()) + " to " + String.valueOf(target));
        }
    }

    private static class InvalidConfigurationParameterValueException extends ConfigurationException {
        public InvalidConfigurationParameterValueException(final String key, Object value, Object from, Object to) {
            super("Parameter \"" + key + "\" (" + String.valueOf(value) +
                ")is outside the valid range [" + String.valueOf(from) + ", " + String.valueOf(to) + "]");
        }
    }

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
     * Creates a {@link ConfigurationDictionary} using the given Java properties.
     * <br><br>
     * This constructor enables a simple mechanism to convert Java Properties or Java properties files to into a
     * configuration dictionary that can be used almost anywhere inside jSCADA.
     *
     * @param properties The properties to take the configuration from.
     */
    public ConfigurationDictionary(final Properties properties) {
        for (Map.Entry entry : properties.entrySet()) {
            this.properties.put(String.valueOf(entry.getKey()), entry.getValue());
        }
    }

    /**
     * Returns the value for the given key if the value exists in the configuration or fails with an
     * ConfigurationException if the configuration does not contain a value with the given key. Note that the method
     * can fail even if a value is present for the given key in the configuration if the type of the actual value in
     * the configuration is not compatible with the requested type.
     *
     * @param key        Configuration option key.
     * @param valueClass Class of which the configuration value has to be.
     * @param <T>        The type of the configuration parameter.
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
            } else if (valueClass.isAssignableFrom(String.class)) {
                return (T)value.toString();
            } else if (Byte.class.isAssignableFrom(value.getClass())) {
                if (valueClass.isAssignableFrom(Short.class)) {
                    return (T) Short.valueOf((byte) value);
                } else if (valueClass.isAssignableFrom(Integer.class)) {
                    return (T) Integer.valueOf((byte) value);
                } else if (valueClass.isAssignableFrom(Long.class)) {
                    return (T) Long.valueOf((byte) value);
                } else if (valueClass.isAssignableFrom(Float.class)) {
                    return (T) Float.valueOf((byte) value);
                } else if (valueClass.isAssignableFrom(Double.class)) {
                    return (T) Double.valueOf((byte) value);
                } else {
                    throw new InvalidConfigurationParameterTypeException(key, value, valueClass);
                }
            } else if (Short.class.isAssignableFrom(value.getClass())) {
                if (valueClass.isAssignableFrom(Integer.class)) {
                    return (T) Integer.valueOf((short) value);
                } else if (valueClass.isAssignableFrom(Long.class)) {
                    return (T) Long.valueOf((short) value);
                } else if (valueClass.isAssignableFrom(Float.class)) {
                    return (T) Float.valueOf((short) value);
                } else if (valueClass.isAssignableFrom(Double.class)) {
                    return (T) Double.valueOf((short) value);
                } else {
                    throw new InvalidConfigurationParameterTypeException(key, value, valueClass);
                }
            } else if (Integer.class.isAssignableFrom(value.getClass())) {
                if (valueClass.isAssignableFrom(Long.class)) {
                    return (T) Long.valueOf((int) value);
                } else if (valueClass.isAssignableFrom(Float.class)) {
                    return (T) Float.valueOf((int) value);
                } else if (valueClass.isAssignableFrom(Double.class)) {
                    return (T) Double.valueOf((int) value);
                } else {
                    throw new InvalidConfigurationParameterTypeException(key, value, valueClass);
                }
            } else if (Long.class.isAssignableFrom(value.getClass())) {
                if (valueClass.isAssignableFrom(Float.class)) {
                    return (T) Float.valueOf((long) value);
                } else if (valueClass.isAssignableFrom(Double.class)) {
                    return (T) Double.valueOf((long) value);
                } else {
                    throw new InvalidConfigurationParameterTypeException(key, value, valueClass);
                }
            } else if (Float.class.isAssignableFrom(value.getClass())) {
                if (valueClass.isAssignableFrom(Double.class)) {
                    return (T) Double.valueOf((float) value);
                } else {
                    throw new InvalidConfigurationParameterTypeException(key, value, valueClass);
                }
            } else if (String.class.isAssignableFrom(value.getClass())) {
                String stringValue = String.valueOf(value);
                try {
                    // Otherwise, in the case the value in the configuration is of type string, try to parse the value...
                    if (valueClass.isAssignableFrom(Boolean.class)) {
                        if (stringValue.toLowerCase(Locale.ROOT).equals("true") ||
                            stringValue.toLowerCase(Locale.ROOT).equals("false")) {
                            return (T) Boolean.valueOf(Boolean.parseBoolean(stringValue));
                        } else {
                            throw new InvalidConfigurationParameterTypeException(key, stringValue, valueClass);
                        }
                    } else if (valueClass.isAssignableFrom(Byte.class)) {
                        return (T) Byte.valueOf(Byte.parseByte(stringValue));
                    } else if (valueClass.isAssignableFrom(Short.class)) {
                        return (T) Short.valueOf(Short.parseShort(stringValue));
                    } else if (valueClass.isAssignableFrom(Integer.class)) {
                        return (T) Integer.valueOf(Integer.parseInt(stringValue));
                    } else if (valueClass.isAssignableFrom(Long.class)) {
                        return (T) Long.valueOf(Long.parseLong(stringValue));
                    } else if (valueClass.isAssignableFrom(Float.class)) {
                        return (T) Float.valueOf(Float.parseFloat(stringValue));
                    } else if (valueClass.isAssignableFrom(Double.class)) {
                        return (T) Double.valueOf(Double.parseDouble(stringValue));
                    } else {
                        // If there is now conversion known, fail.
                        throw new InvalidConfigurationParameterTypeException(key, value, valueClass);
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidConfigurationParameterTypeException(key, value, valueClass);
                }
            } else {
                // All other types can not be processed.
                throw new InvalidConfigurationParameterTypeException(key, value, valueClass);
            }

            // If the value is missing, fail with an MissingConfigurationParameterException.
        } else {
            throw new MissingConfigurationParameterException(key);
        }
    }

    /**
     * Returns the value for the given key if the value exists in the configuration dictionary or returns the default
     * value if the configuration dictionary does not contain a value with the given key. Note that if a value is
     * present in the configuration, it is checked that the type of the actual value in the configuration can be
     * converted to the requested data type and if that is not the case, an exception is thrown.
     *
     * @param key          Configuration option key.
     * @param defaultValue The default value to use in the absence of a value in the configuration.
     * @param <T>          The type of the configuration parameter.
     * @return The configured value or the default value in the absence of a value in the configuration dictionary for
     * the given key.
     * @throws ConfigurationException This exception is thrown if the type of the value in the configuration does not
     *                                match the default value's type or the value can not be converted to the same type.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final String key, final T defaultValue) throws ConfigurationException {
        if (defaultValue != null) {
            try {
                return (T) get(key, defaultValue.getClass());
            } catch (MissingConfigurationParameterException e) {
                return defaultValue;
            }
        } else {
            throw new ConfigurationException("\"null\" is an invalid default value!");
        }
    }

    /**
     * Returns the value for the given key if the value exists in the configuration dictionary and if it in the given
     * range. If a value is present in the configuration, it is checked that the type of the actual value in the
     * configuration can be converted to the data type of the range specifiers and if that is the case, it is checked
     * that the value is in the supplied range (including from and to). If not, an exception is thrown.
     *
     * @param key   Configuration option key.
     * @param from  The smallest valid value (including the value itself).
     * @param to    The biggest valid value (including the value itself).
     * @param <T>   The type of the configuration parameter.
     * @return The configured value if such a value is present in the dictionary and is inside the specified range.
     * @throws ConfigurationException This exception is thrown if the type of the value in the configuration does not
     *                                match the range's start and endpoints or the value is outside the valid range.
     */
    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> T getInRange(final String key, final T from, final T to)
        throws ConfigurationException {
        T value = (T) get(key, from.getClass());
        if (value.compareTo(from) < 0 || value.compareTo(to) > 0) {
            throw new InvalidConfigurationParameterValueException(key, value, from, to);
        }
        return value;
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
