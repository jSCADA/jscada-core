package ch.hevs.jscada.factory;

import ch.hevs.jscada.ScadaSystem;
import ch.hevs.jscada.config.ConfigurationDictionary;
import ch.hevs.jscada.config.ConfigurationException;
import ch.hevs.jscada.io.Connection;
import ch.hevs.jscada.io.ConnectionInitializeException;
import ch.hevs.jscada.io.field.FieldConnection;
import ch.hevs.jscada.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.Manifest;

/**
 * Abstract factory in order to load a jSCADA system from different locations like for example SCADA definition files,
 * databases or any other imaginable source.
 * <br><br>
 * jSCADA offers some build-in Factory classes that support different configuration sources, but you can add your own
 * SCADA system loading mechanism by subclassing {@link ScadaSystemFactory} and implementing the abstract methods. If
 * you provide your own factory implementation, you have to guarantee that the factory class is in the classpath and
 * you will need to pass the class name including package path to the {@link ScadaSystemFactory#load} as the type.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public abstract class ScadaSystemFactory {
    private static final Logger log = LoggerFactory.getLogger(ScadaSystemFactory.class);
    private static final String JAR_FILE = new java.io.File(ScadaSystemFactory.class.getProtectionDomain()
        .getCodeSource()
        .getLocation()
        .getPath())
        .getName();
    private ScadaSystem system;

    /**
     * Main entry point in order to load a standalone jSCADA system.
     * <br><br>
     * The first parameter passed to the main method defines the actual Factory implementation to use in order to load
     * the jSCADA system from an existing definition. All other parameters have to be in the form
     * <b>--{key}={value}</b>.
     * <br><br>
     * An example command line to start a jSCADA system could be like this:
     * <br><br>
     * {@code
     * java -jar jscada-core-runnable-{version}.jar xml --file=text.xml
     * }
     * <br>
     * This would load a jSCADA system from an XML file using the build-in xml factory from the file text.xml.
     *
     * @param args Arguments passed to the SCADA system factory.
     */
    public static void main(String... args) {
        if (args.length == 0 || args.length == 1 && args[0].equals("help")) {
            StringBuilder usageBuilder = new StringBuilder();
            usageBuilder
                .append("Usage:\njava -jar ").append(JAR_FILE).append(" <factory> [properties]\n")
                .append("  Creates and starts a jSCADA system using the specified factory class.\n")
                .append("    <factory>: Factory to use in order to construct the jSCADA system. Can be either an\n")
                .append("               identifier for a build-in factory or the canonical class name of a\n")
                .append("               custom external factory. The custom factory has to be in the classpath.\n")
                .append("               Available build-in factories: xml\n\n")
                .append("    [properties]: Properties passed to the factory in the form of --<key>=<value>\n\n")
                .append("java -jar ").append(JAR_FILE).append(" help\n")
                .append("  Shows this help screen.\n\n")
                .append("java -jar ").append(JAR_FILE).append(" help <factory>\n")
                .append("  Shows the help screen of the given build-in or external jSCADA factory if available.\n");
            System.out.println(usageBuilder);
            return;
        } else if (args.length == 2 && args[0].equals("help")) {
            ScadaSystemFactory factory;
            try {
                factory = getFactory(args[1]);
                StringBuilder usageBuilder = new StringBuilder();
                usageBuilder.append("Usage:\njava -jar ").append(JAR_FILE).append(" ").append(args[1]).append(" ");
                factory.getUsage(usageBuilder);
                usageBuilder.append("\n");
                System.out.println(usageBuilder);
            } catch (IOException e) {
                System.out.println("Invalid factory identifier!");
            }
            return;
        }

        try {
            log.info("Starting jSCADA {}in standalone configuration...", getVersion());

            // Load the system using the program arguments.
            final ScadaSystem system = load(args);

            if (system != null) {
                // Start the SCADA system.
                system.start();

                // Add a hook in order to shutdown the SCADA system upon application end.
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        log.info("Shutting down...");
                        system.stop();
                    }
                });
            } else {
                log.error("System loading failed due to an unknown error, sorry :-/.");
            }
        } catch (Exception e) {
            log.error("Exception during SCADA system loading!", e);
        }
    }

    /**
     * Convenience load method. The first argument has to be the identifier of the SCADA system factory to use. For
     * example "xml" to load the SCADA system from an XML file.
     * <br><br>
     * All other arguments have to be key/value pairs in the following format --{key}={value} and will be passed to the
     * SCADA system loader implementation as a {@link ConfigurationDictionary}.
     *
     * @param args Arguments for loading a SCADA system.
     * @return A reference to the created jSCADA system.
     * @throws Exception Any exception can be thrown by the different jSCADA loader implementations.
     */
    public static ScadaSystem load(String... args) throws Exception {
        // The first parameter always defines the type of loading mechanism.
        if (args.length >= 1) {

            // Create a configuration dictionary using the rest of the given arguments.
            final ConfigurationDictionary configuration =
                new ConfigurationDictionary(Arrays.copyOfRange(args, 1, args.length));

            // Call designated load method.
            return load(args[0], configuration);
        } else {
            throw new ConfigurationException("Missing the mandatory SCADA factory identifier!");
        }
    }

    /**
     * Designated jSCADA system loader. Uses the given factory and configuration in order to be able load a SCADA
     * system from different sources.
     *
     * @param identifier    The identifier of the factory to use. Example "xml" for xml files or canonical class names.
     * @param configuration Configuration parameters for the selected SCADA system loader.
     * @return A ready to use SCADA system.
     * @throws Exception Any exception can be thrown by the different jSCADA loader implementations.
     */
    public static ScadaSystem load(final String identifier, ConfigurationDictionary configuration) throws Exception {
        ScadaSystemFactory factory = getFactory(identifier);

        // Create a new SCADA system.
        factory.system = new ScadaSystem();

        // Call the factory's load implementation.
        factory.loadImplementation(configuration);

        // Return the SCADA system.
        return factory.system;
    }

    /**
     * This method has to be implemented by a SCADA system factory class.
     * <br><br>
     * The method should load the SCADA system using the API (protected methods) provided by this class. The factory
     * does not have direct access to the SCADA system instance, it can be modified only by using the protected
     * methods of {@link ScadaSystemFactory}.
     *
     * @param configuration Configuration parameters.
     * @throws Exception Any exception will be propagated further.
     */
    protected abstract void loadImplementation(ConfigurationDictionary configuration) throws Exception;

    /**
     * This method can be implemented by a SCADA system factory class in order to provide a simple usage guide on the
     * console.
     * <br><br>
     * The text appended to the given string builder will be used to print usage help information on the console.
     *
     * @param builder {@link StringBuilder} that can be used to add the Factory's usage information.
     *                Usage text to display on console.
     */
    protected void getUsage(StringBuilder builder) {
        builder
            .append("\n  ")
            .append(getClass().getCanonicalName())
            .append(" does not provide any help.");
    }

    /**
     * Sets the synchronization interval on the target SCADA system to the given value.
     *
     * @param interval Synchronization-interval in milliseconds.
     */
    protected final void setSynchronisationInterval(int interval) {
        system.setSynchronizationInterval(interval);
    }

    /**
     * Creates and returns a data point with the given type and ID.
     *
     * @param type        The type the data point has to have.
     * @param id          ID, should be unique within the process.
     * @param useExisting If a data point with the same ID and type already exists, this data point will
     *                    be returned and no exception will be thrown. If the type does not match, an
     *                    exception will be thrown, even when this parameter is set to true.
     * @return Reference to the created/existing data point.
     * @throws DuplicateIdException In the case a (incompatible) data point already exists, this exception is
     *                              thrown.
     */
    private DataPoint createDataPoint(final DataPointType type, final String id, boolean useExisting)
        throws DuplicateIdException {
        // If we are allowed to use existing data points...
        if (useExisting) {
            // ... get the reference to the data point with the given ID.
            final DataPoint dataPoint = system.getProcess().getDataPoint(id);

            // If the data point exists...
            if (dataPoint != null) {
                // ... and the type matches.
                if (dataPoint.getType() == type) {
                    // ... return the existing data point.
                    return dataPoint;
                } else {
                    // A data point exists, but is not of the same type as the requested one!
                    throw new DuplicateIdException("Datapoint \"" + id + "\" already exists with another data type!");
                }
            }
        }

        // No data point found or existing should not be used, so we create a new data point with the given type.
        switch (type) {
            case BOOLEAN:
                return new BooleanDataPoint(id, system.getProcess());

            case INTEGER:
                return new IntegerDataPoint(id, system.getProcess());

            case FLOATING_POINT:
                return new FloatDataPoint(id, system.getProcess());

            default:
                throw new IllegalArgumentException("Invalid data type specified!");
        }
    }

    /**
     * Creates a new connection instance and adds the instance to the connection group of the SCADA system.
     *
     * @param clazz         Class name of the connection to instantiate.
     * @param id            ID to give to the connection, note that the ID should be unique.
     * @param configuration Configuration to pass to the initialize method of the connection.
     * @return Returns the created connection.
     * @throws DuplicateIdException          If there already exists a connection with the given ID.
     * @throws ClassNotFoundException        The given class for the connection can not be found by the class loader.
     * @throws InstantiationException        It was not possible to instantiate an object of the give class.
     * @throws IllegalAccessException        The class loader did not had access to instantiate the object.
     * @throws ConfigurationException        The connection could not be initialized due an configuration error.
     * @throws ConnectionInitializeException The connection could not be initialized due an runtime error.
     */
    protected final FieldConnection createConnection(final String clazz, final String id,
                                                     final ConfigurationDictionary configuration)
        throws DuplicateIdException, ClassNotFoundException, InstantiationException, IllegalAccessException,
        ConfigurationException, ConnectionInitializeException {
        // If there is already a connection with the given ID present, fail.
        if (system.getConnections().getConnection(id) != null) {
            throw new DuplicateIdException(id);
        }

        // Try to load the class.
        final Class<?> connectionClass = getClass().getClassLoader().loadClass(clazz);

        // Check that the connection implements the FieldConnection interface.
        if (FieldConnection.class.isAssignableFrom(connectionClass)) {
            // Create the instance, add it to the connection group.
            final FieldConnection connection = (FieldConnection) connectionClass.newInstance();
            system.getConnections().addConnection(id, connection);

            // Try to initialize the connection.
            connection.initialize(configuration, system);
            return connection;
        } else {
            throw new InstantiationException("FieldConnection class does not implement the ch.hevs.scada.io.Connector" +
                " interface");
        }
    }

    /**
     * Returns the connection with the given ID.
     *
     * @param id ID of the connection to return.
     * @return Reference to the connection or null if the connection with the given ID does not exist.
     */
    protected final Connection getConnection(final String id) {
        return system.getConnections().getConnection(id);
    }

    /**
     * Adds an input to the given SCADA fieldConnection and connects that input to the given data point. Note that only
     * one input can be connected at the same time, otherwise the method will fail with an error. Note that if the data
     * point does not already exists, it will be created on the fly by this method.
     *
     * @param fieldConnection The fieldConnection to which the input has to be added.
     * @param dataPointType   Type of the target data point.
     * @param dataPointId     ID if the data point to use as target for the input.
     * @param configuration   Input configuration parameters.
     * @throws ConfigurationException If the configuration is incomplete or invalid or the data point is already in
     *                                use.
     * @throws DuplicateIdException   If there exists a data point with the same ID but another type.
     */
    protected final void addInput(final FieldConnection fieldConnection, final DataPointType dataPointType,
                                  final String dataPointId, final ConfigurationDictionary configuration)
        throws ConfigurationException, DuplicateIdException {
        // FieldConnection must be valid!
        if (fieldConnection == null) {
            throw new ConfigurationException("Invalid fieldConnection reference!");
        }

        // Create or get the data point.
        final DataPoint dataPoint = createDataPoint(dataPointType, dataPointId, false);

        // Should never happen, but we never know...
        if (dataPoint == null) {
            throw new ConfigurationException("Invalid datapoint \"" + dataPointId + "\"");
        }

        // It is an input, so we have to permanently select the data point.
        try {
            dataPoint.select(fieldConnection);
        } catch (SelectException e) {
            throw new ConfigurationException("Datapoint \"" + dataPointId + "\" used by multiple connectors as input!");
        }

        // Add the input to the fieldConnection.
        fieldConnection.addInput(dataPoint, configuration);
    }

    /**
     * Adds an output to the given SCADA fieldConnection and connects that output to the given data point. Note that
     * multiple outputs can be connected at the same time. If the data point does not already exists, it will be
     * created on the fly by this method.
     *
     * @param fieldConnection The fieldConnection to which the output has to be added.
     * @param dataPointType   Type of the source data point.
     * @param dataPointId     ID if the data point to use as source for the output.
     * @param configuration   Output configuration parameters.
     * @throws ConfigurationException If the configuration is incomplete or invalid.
     * @throws DuplicateIdException   If there exists a data point with the same ID but another type.
     */
    protected final void addOutput(final FieldConnection fieldConnection, final DataPointType dataPointType,
                                   final String dataPointId, final ConfigurationDictionary configuration)
        throws ConfigurationException, DuplicateIdException {
        // FieldConnection must be valid!
        if (fieldConnection == null) {
            throw new ConfigurationException("Invalid fieldConnection reference!");
        }

        // Create or get the data point.
        final DataPoint dataPoint = createDataPoint(dataPointType, dataPointId, true);

        // Should never happen, but we never know...
        if (dataPoint == null) {
            throw new ConfigurationException("Invalid datapoint \"" + dataPointId + "\"");
        }

        // Add the output to the connector.
        fieldConnection.addOutput(dataPoint, configuration);
    }

    private static ScadaSystemFactory getFactory(String identifier) throws IOException {
        // First validate all build-in SCADA factories...
        if (identifier.equalsIgnoreCase("xml")) {
            // Create XML factory.
            return new XmlScadaSystemFactory();
        } else {
            // If none of the build-in factories did match the factory type string, try to load a class with that name.
            final Class factoryClass;
            try {
                factoryClass = Thread.currentThread().getContextClassLoader().loadClass(identifier);
                return (ScadaSystemFactory) factoryClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new IOException("SCADA Factory \"" + identifier + "\" not found in classpath!");
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IOException("Error during instantiation of \"" + identifier + "\"!");
            }
        }
    }

    private static String getVersion() {
        try {
            Enumeration<URL> resources = ScadaSystemFactory.class.getClassLoader()
                .getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                try {
                    Manifest manifest = new Manifest(resources.nextElement().openStream());
                    if (ScadaSystemFactory.class.getCanonicalName().equals(
                        manifest.getMainAttributes().getValue("Main-Class"))) {
                        return "(" + manifest.getMainAttributes().getValue("Implementation-Version") + ") ";
                    }
                } catch (IOException e) {
                    log.trace("Exception during version reading: ", e);
                }
            }
        } catch (IOException e) {
            log.trace("Exception during version reading: ", e);
        }

        return "";
    }
}
