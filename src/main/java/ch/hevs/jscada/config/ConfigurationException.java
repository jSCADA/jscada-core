package ch.hevs.jscada.config;

/**
 * This exception is thrown if either a mandatory configuration parameter is missing or either it's type or value
 * are invalid.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
@SuppressWarnings("serial")
public class ConfigurationException extends Exception {
    /**
     * The cause why the exception was thrown is not known.
     *
     * @see ConfigurationException#getExceptionCause()
     */
    public static final int UNKNOWN_CAUSE = 0;

    /**
     * The {@link ConfigurationException} was thrown because a mandatory parameter is missing.
     *
     * @see ConfigurationException#getExceptionCause()
     */
    public static final int MISSING_CONFIGURATION_PARAMETER = 1;

    /**
     * The {@link ConfigurationException} was thrown because the type of a configuration parameter does not match the
     * expected type of can not be converted.
     *
     * @see ConfigurationException#getExceptionCause()
     */
    public static final int INVALID_CONFIGURATION_PARAMETER_TYPE = 2;

    /**
     * The {@link ConfigurationException} was thrown because the value of the configuration parameter is not valid.
     *
     * @see ConfigurationException#getExceptionCause()
     */
    public static final int INVALID_CONFIGURATION_PARAMETER_VALUE = 3;

    // The cause for the configuration exception. Defaults to unknown.
    private int cause = UNKNOWN_CAUSE;

    /**
     * Creates a {@link ConfigurationException} with the given description message and an unknown cause.
     *
     * @param message Exception description.
     */
    public ConfigurationException(final String message) {
        super(message);
    }

    /**
     * Returns the cause of the exception.
     *
     * @return Cause that triggered the throwing of the exception.
     * @see ConfigurationException#UNKNOWN_CAUSE
     * @see ConfigurationException#MISSING_CONFIGURATION_PARAMETER
     * @see ConfigurationException#INVALID_CONFIGURATION_PARAMETER_TYPE
     * @see ConfigurationException#INVALID_CONFIGURATION_PARAMETER_VALUE
     */
    public int getExceptionCause() {
        return cause;
    }

    /**
     * Creates a new exception for a missing configuration parameter.
     *
     * @param key The key of the missing configuration parameter.
     * @return ConfigurationException.
     */
    public static ConfigurationException missingConfigurationParameter(final String key) {
        ConfigurationException exception = new ConfigurationException(
            String.format("Missing mandatory configuration parameter \"%s\"", key));
        exception.cause = MISSING_CONFIGURATION_PARAMETER;
        return exception;
    }

    /**
     * Creates a new exception in order to report an invalid configuration parameter type.
     *
     * @param key    The key of the invalid configuration parameter.
     * @param value  The value of the invalid configuration parameter.
     * @param target The target type of the invalid configuration parameter.
     * @return ConfigurationException.
     */
    public static ConfigurationException invalidConfigurationParameterType(final String key, final Object value,
                                                                           final Class<?> target) {
        ConfigurationException exception = new ConfigurationException(
            String.format("Impossible to convert parameter \"%s\" (value=%s) of %s to %s",
                key, String.valueOf(value), String.valueOf(value.getClass()), String.valueOf(target)));
        exception.cause = INVALID_CONFIGURATION_PARAMETER_TYPE;
        return exception;
    }

    /**
     * Creates a new exception in order to report an invalid configuration parameter value.
     *
     * @param key   The key of the invalid configuration parameter.
     * @param value The value of the invalid configuration parameter.
     * @return ConfigurationException.
     */
    public static ConfigurationException invalidConfigurationParameterValue(final String key, final Object value) {
        ConfigurationException exception = new ConfigurationException(String.format("Parameter \"%s\" (%s) is invalid",
            key, String.valueOf(value)));
        exception.cause = INVALID_CONFIGURATION_PARAMETER_VALUE;
        return exception;
    }

    /**
     * Creates a new exception in order to report an invalid configuration parameter value.
     *
     * @param key   The key of the invalid configuration parameter.
     * @param value The value of the invalid configuration parameter.
     * @param from  The minimal value of the valid range.
     * @param to    The maximal value of the valid range.
     * @return ConfigurationException.
     */
    public static ConfigurationException invalidConfigurationParameterValue(final String key, final Object value,
                                                                            final Object from, final Object to) {
        ConfigurationException exception = new ConfigurationException(
            String.format("Parameter \"%s\" (%s)is outside the valid range [%s, %s]",
                key, String.valueOf(value), String.valueOf(from), String.valueOf(to)));
        exception.cause = INVALID_CONFIGURATION_PARAMETER_VALUE;
        return exception;
    }

    /**
     * Creates a new exception in order to report an invalid configuration parameter value.
     *
     * @param key        The key of the invalid configuration parameter.
     * @param value      The value of the invalid configuration parameter.
     * @param valuesList List of all values that would be valid.
     * @return ConfigurationException.
     */
    public static ConfigurationException invalidConfigurationParameterValue(final String key, final Object value,
                                                                            final String valuesList) {
        ConfigurationException exception = new ConfigurationException(
            String.format("Invalid value for parameter \"%s\" (%s), valid values are: [%s]",
                key, String.valueOf(value), valuesList));
        exception.cause = INVALID_CONFIGURATION_PARAMETER_VALUE;
        return exception;
    }
}
