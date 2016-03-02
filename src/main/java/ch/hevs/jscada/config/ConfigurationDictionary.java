package ch.hevs.jscada.config;

import org.xml.sax.Attributes;

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
    // Actual parameter storage.
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
     * Creates a {@link ConfigurationDictionary} using the given Java properties.
     * <br><br>
     * This constructor enables a simple mechanism to convert Java Properties or Java properties files into a
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
     * Creates a {@link ConfigurationDictionary} using the given SAX Attributes properties.
     * <br><br>
     * This constructor enables a simple mechanism to convert SAX Attributes into a configuration dictionary that can
     * be used almost anywhere inside jSCADA.
     *
     * @param attributes SAX Attributes to take the configuration from.
     */
    public ConfigurationDictionary(final Attributes attributes) {
        for (int i = 0; i < attributes.getLength(); ++i) {
            properties.put(attributes.getQName(i), attributes.getValue(i));
        }
    }

    /**
     * Returns the value for the given key if the value exists in the configuration or fails with an
     * ConfigurationException if the configuration does not contain a value with the given key. Note that the method
     * can fail even if a value is present for the given key in the configuration if the type of the actual value in
     * the configuration is not compatible with the requested type.
     * <br><br>
     * Supported (tested) classes/types are:
     * <ul>
     * <li>Boolean & boolean</li>
     * <li>Byte and byte</li>
     * <li>Short and short</li>
     * <li>Integer and int</li>
     * <li>Long and long</li>
     * <li>Float and float</li>
     * <li>Double and double</li>
     * <li>String</li>
     * <li>Any Enumeration</li>
     * </ul>
     * <br><br>
     * All numbers can be converted from one to another as long as the size of the target type is bigger than the size
     * of the source type. This means it is possible to convert an int to a long, but not in the other direction. If
     * the target type is a number or an Enum type, the {@link ConfigurationDictionary} tries to parse the string and
     * convert the string's value into the target number/enum type. If the parsing fails, a
     * {@link ConfigurationException} will be thrown.
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
                return (T) value.toString();
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
                    throw ConfigurationException.invalidConfigurationParameterType(key, value, valueClass);
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
                    throw ConfigurationException.invalidConfigurationParameterType(key, value, valueClass);
                }
            } else if (Integer.class.isAssignableFrom(value.getClass())) {
                if (valueClass.isAssignableFrom(Long.class)) {
                    return (T) Long.valueOf((int) value);
                } else if (valueClass.isAssignableFrom(Float.class)) {
                    return (T) Float.valueOf((int) value);
                } else if (valueClass.isAssignableFrom(Double.class)) {
                    return (T) Double.valueOf((int) value);
                } else {
                    throw ConfigurationException.invalidConfigurationParameterType(key, value, valueClass);
                }
            } else if (Long.class.isAssignableFrom(value.getClass())) {
                if (valueClass.isAssignableFrom(Float.class)) {
                    return (T) Float.valueOf((long) value);
                } else if (valueClass.isAssignableFrom(Double.class)) {
                    return (T) Double.valueOf((long) value);
                } else {
                    throw ConfigurationException.invalidConfigurationParameterType(key, value, valueClass);
                }
            } else if (Float.class.isAssignableFrom(value.getClass())) {
                if (valueClass.isAssignableFrom(Double.class)) {
                    return (T) Double.valueOf((float) value);
                } else {
                    throw ConfigurationException.invalidConfigurationParameterType(key, value, valueClass);
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
                            throw ConfigurationException.invalidConfigurationParameterType(key, value, valueClass);
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
                    } else if (valueClass.isEnum()) {
                        return (T) Enum.valueOf((Class<Enum>) valueClass, stringValue);
                    } else {
                        // If there is now conversion known, fail.
                        throw ConfigurationException.invalidConfigurationParameterType(key, value, valueClass);
                    }
                } catch (IllegalArgumentException e) {
                    throw ConfigurationException.invalidConfigurationParameterType(key, value, valueClass);
                }
            } else {
                // All other types can not be processed.
                throw ConfigurationException.invalidConfigurationParameterType(key, value, valueClass);
            }

            // If the value is missing, fail with an MissingConfigurationParameterException.
        } else {
            throw ConfigurationException.missingConfigurationParameter(key);
        }
    }

    /**
     * Returns the value for the given key if the value exists in the configuration dictionary or returns the default
     * value if the configuration dictionary does not contain a value with the given key. Note that if a value is
     * present in the configuration, it is checked that the type of the actual value in the configuration can be
     * converted to the requested data type and if that is not the case, an exception is thrown.
     * <br><br>
     * Supported (tested) classes/types are:
     * <ul>
     * <li>Boolean & boolean</li>
     * <li>Byte and byte</li>
     * <li>Short and short</li>
     * <li>Integer and int</li>
     * <li>Long and long</li>
     * <li>Float and float</li>
     * <li>Double and double</li>
     * <li>String</li>
     * <li>Any Enumeration (You can actually provide an enumeration value as default value)</li>
     * </ul>
     * <br><br>
     * All numbers can be converted from one to another as long as the size of the target type is bigger than the size
     * of the source type. This means it is possible to convert an int to a long, but not in the other direction. If
     * the target type is a number or an Enum type, the {@link ConfigurationDictionary} tries to parse the string and
     * convert the string's value into the target number/enum type. If the parsing fails, a
     * {@link ConfigurationException} will be thrown.
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
            } catch (ConfigurationException e) {
                if (e.getExceptionCause() == ConfigurationException.MISSING_CONFIGURATION_PARAMETER) {
                    return defaultValue;
                } else {
                    throw e;
                }
            }
        } else {
            throw new ConfigurationException("\"null\" is an invalid default value!");
        }
    }

    /**
     * Interface for configuration parameter validity checkers. An object implementing this interface can be passed
     * to the method {@link ConfigurationDictionary#get(String, ValidityChecker)} and the validity of the parameter is
     * automatically checked using the supplied validity checker by calling the methods
     * {@link ValidityChecker#getType()} and {@link ValidityChecker#validate(String, Object)}. If the actual parameter
     * type can not be converted into the requested type an exception will be thrown. If the value could be converted,
     * the method {@link ValidityChecker#validate(String, Object)} of the passed validity checker will be called. If the
     * method does not throw an exception, the configuration parameter value will be considered as valid, if the
     * validity checker will signal an actual error, he has to throw a {@link ConfigurationException}.
     * <br><br>
     * {@link ConfigurationDictionary} offers some build-in validity checker implementations for ranges
     * {@link ConfigurationDictionary#inRange(Comparable, Comparable)} and the mandatory presence in a set with
     * {@link ConfigurationDictionary#inSet(Comparable[])}.
     *
     * @param <T> Class or type the validity checker is working with.
     */
    public interface ValidityChecker<T> {
        /**
         * This method should return the actual type of the value that will be checked by the validity checker. Note
         * that this will be used to convert the configuration parameter into the right type before passing to the
         * validation process.
         *
         * @return Type or class the validity checker works on.
         */
        Class getType();

        /**
         * This method is called inside the method {@link ConfigurationDictionary#get(String, ValidityChecker)} just
         * after the value has been read from the configuration and converted into the target type. The method should
         * throw an exception to signal that the value was not accepted by the validator, if the value is acceptable,
         * the method should not throw an exception and return. Note that the message passed to the exception should
         * explain why the value was not validated.
         *
         * @param key   Configuration option key.
         * @param value The value of the configuration parameter to validate.
         * @throws ConfigurationException The method should throw a ConfigurationException if the validation of the
         *                                value was not successful.
         */
        void validate(final String key, final T value) throws ConfigurationException;
    }

    /**
     * Returns a {@link ValidityChecker} that ensures that the provided value is in the given range.
     *
     * @param from Start of the valid range (including the value itself).
     * @param to   End of the valid range (including the value itself).
     * @param <T>  Class or type the validity checker is working with.
     * @return {@link ValidityChecker} ensuring the configuration value is in the given range.
     */
    public static <T extends Comparable<T>> ValidityChecker<T> inRange(final T from, final T to) {
        assert (from != null);
        assert (to != null);
        assert (from.compareTo(to) <= 0);
        assert (from.getClass() != String.class);

        return new ValidityChecker<T>() {
            @Override
            public Class getType() {
                return from.getClass();
            }

            @Override
            public void validate(String key, T value) throws ConfigurationException {
                if (value != null) {
                    if (value.compareTo(from) < 0 || value.compareTo(to) > 0) {
                        throw ConfigurationException.invalidConfigurationParameterValue(key, value, from, to);
                    }
                } else {
                    throw ConfigurationException.invalidConfigurationParameterValue(key, null);
                }
            }
        };
    }

    /**
     * Returns a {@link ValidityChecker} that ensures that the provided value is in the given set of values.
     *
     * @param set Set of valid values.
     * @param <T> Class or type the validity checker is working with.
     * @return {@link ValidityChecker} ensuring the configuration value is in the given set.
     */
    @SafeVarargs
    public static <T extends Comparable<T>> ValidityChecker<T> inSet(final T... set) {
        assert (set.length > 0);

        return new ValidityChecker<T>() {
            @Override
            public Class getType() {
                return set[0].getClass();
            }

            @Override
            public void validate(String key, T value) throws ConfigurationException {
                for (T element : set) {
                    if (value.compareTo(element) == 0) {
                        return;
                    }
                }
                StringBuilder validValues = new StringBuilder();
                for (int i = 0; i < set.length; ++i) {
                    validValues.append(String.valueOf(set[i]));
                    if (i != set.length - 1) {
                        validValues.append(", ");
                    }
                }
                throw ConfigurationException.invalidConfigurationParameterValue(key, value, validValues.toString());
            }
        };
    }

    /**
     * Returns the value for the given key if the value exists in the configuration dictionary and if if the given
     * validity checker does not throw an exception. If a value is present in the configuration, it is checked that
     * the type of the actual value in the configuration can be converted to the data type of the validity checker
     * and if that is the case, it is checked that the value is accepted by the checker.
     *
     * @param key     Configuration option key.
     * @param checker Parameter validity checker.
     * @param <T>     The type of the configuration parameter.
     * @return The configured value if such a value is present in the dictionary and is inside the specified range.
     * @throws ConfigurationException This exception is thrown if the type of the value in the configuration does not
     *                                match the range's type or if the value is outside the valid range.
     */
    public <T extends Comparable<T>> T get(final String key, final ValidityChecker<T> checker)
        throws ConfigurationException {
        @SuppressWarnings("unchecked")
        T value = (T) get(key, checker.getType());
        checker.validate(key, value);
        return value;
    }

    /**
     * Returns true if the configuration dictionary contains a value for the given key, false if not.
     *
     * @param key Key to validate.
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
     * @return Reference to the configuration dictionary self in order to enable chaining of method calls.
     */
    public <T> ConfigurationDictionary set(final String key, final T value) {
        properties.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        for (Map.Entry entry : properties.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
        }
        builder.append("}");
        return builder.toString();
    }
}
