package ch.hevs.jscada.config;

import ch.hevs.jscada.exception.ConfigurationException;
import ch.hevs.jscada.exception.ConnectionInitializeException;
import ch.hevs.jscada.exception.DuplicateIdException;
import ch.hevs.jscada.io.Connection;
import ch.hevs.jscada.model.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * Abstract Factory in order to load a jSCADA system from different locations like for example XML files or databases.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public abstract class ScadaSystemFactory {
	
	/**
	 * Convenience load method. The first argument has to be the identifier of the SCADA system loader to use. For 
	 * example "xml" to load the SCADA system from an XML file or "db" in order to load the database definition from 
	 * a database.
	 * 
	 * All other arguments have to be key/value pairs in the following format {key}={value} and will be passed to the 
	 * SCADA system loader implementation.
	 * 
	 * @param args			Arguments for loading a SCADA system.
	 * @return				A reference to the created jSCADA system.
	 * @throws Exception	Any exception can be thrown by the different jSCADA loader implementations.
	 */
	public static ScadaSystem load(String... args) throws Exception {
		// The first parameter always defines the type of loading mechanism.
		if (args.length > 1) {
			
			// Create a configuration dictionary using the rest of the given arguments.
			final ConfigurationDictionary configuration = 
					new ConfigurationDictionary(Arrays.copyOfRange(args, 1, args.length));
			
			// Call designated load method.
			return load(args[0], configuration);
		}
		
		return null;
	}
	
	/**
	 * Designated jSCADA system loader. Uses the given loader type and configuration in order to load a SCADA system
	 * from different sources.
	 * 
	 * @param type				The type of loader to use. Example "xml" for xml files, "db" for database.
	 * @param configuration		Configuration parameters for the selected SCADA system loader.
	 * @return					A ready to use SCADA system.
	 * @throws Exception		Any exception can be thrown by the different jSCADA loader implementations.
	 */
	public static ScadaSystem load(final String type, ConfigurationDictionary configuration) throws Exception {
		ScadaSystemFactory factory = null;
		
		// Do we have to load the SCADA system from an XML file?
		if (type.toLowerCase().equals("xml")) {
			// Create XML factory.
			factory = new XmlScadaSystemFactory();
		}
		
		if (factory == null) {
			throw new IOException("Loader \"" + type + "*\" not supported!");
		}
		
		// Create a new SCADA system.
		factory.system = new ScadaSystem();
		
		// Call the factory's load implementation.
		factory.loadImplementation(configuration);
		
		// Return the SCADA system.
		return factory.system;
	}

	/**
	 * This method has to be implemented by a SCADA system loader class.
	 * 
	 * @param configuration	Configuration parameters.
	 * @throws Exception	Any exception will be propagated further.
	 */
	protected abstract void loadImplementation(ConfigurationDictionary configuration) throws Exception;
	
	/**
	 * Sets the synchronization interval on the target SCADA system to the given value.
	 * 
	 * @param interval	Synchronization-interval in milliseconds.
	 */
	protected void setSynchronisationInterval(int interval) {
		system.setSynchronizationInterval(interval);
	}
	
	/**
	 * Creates and returns a data point with the given type and ID.
	 * 
	 * @param type						The type the data point has to have.
	 * @param id						ID, should be unique within the process.
	 * @param useExisting				If a data point with the same ID and type already exists, this data point will
	 * 									be returned and no exception will be thrown. If the type does not match, an 
	 * 									exception will be thrown, even when this parameter is set to true.
	 * @return							Reference to the created/existing data point.
	 * @throws DuplicateIdException		In the case a (incompatible) data point already exists, this exception is 
	 * 									thrown.
	 */
	protected DataPoint createDataPoint(final DataPointType type, final String id, boolean useExisting) 
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
					throw new DuplicateIdException("Datapoint \"" + id + "\" already exists with another datatype!");
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
				return null;
		}
	}
	
	/**
	 * Creates a new connection instance and adds the instance to the connection group of the SCADA system.
	 * 
	 * @param clazz								Class name of the connection to instantiate.
	 * @param id								ID to give to the connection, note that the ID should be unique.
	 * @param configuration						Configuration to pass to the initialize method of the connection.
	 * @return									Returns the created connection.
	 * @throws DuplicateIdException				If there already exists a connection with the given ID.
	 * @throws ClassNotFoundException			The given class for the connection can not be found by the classloader.
	 * @throws InstantiationException			It was not possible to instantiate an object of the give class.
	 * @throws IllegalAccessException			The class loader did not had access to instantiate the object.
	 * @throws ConfigurationException			The connection could not be initialized due an configuration error.
	 * @throws ConnectionInitializeException	The connection could not be initialized due an runtime error.
	 */
	protected Connection createConnection(final String clazz, final String id, 
			final ConfigurationDictionary configuration) 
			throws DuplicateIdException, ClassNotFoundException, InstantiationException, IllegalAccessException, 
			ConfigurationException, ConnectionInitializeException {
		// If there is already a connection with the given ID present, fail.
		if (system.getConnections().getConnection(id) != null) {
			throw new DuplicateIdException(id);
		}
		
		// Try to load the class.
		final Class<?> connectionClass = getClass().getClassLoader().loadClass(clazz);
		
		// Check that the connection implements the Connection interface.
		if (Connection.class.isAssignableFrom(connectionClass)) {
			// Create the instance, add it to the connection group.
			final Connection connection = (Connection)connectionClass.newInstance();
			system.getConnections().addConnection(id, connection);
			
			// Try to initialize the connection.
			connection.initialize(configuration);
			return connection;
		} else {
			throw new InstantiationException("Connection class does not implement the ch.hevs.scada.io.Connector" + 
					" interface");
		}
	}
	
	/**
	 * Returns the connection with the given ID.
	 * 
	 * @param id	ID of the connection to return.
	 * @return		Reference to the connection or null if the connection with the given ID does not exist.
	 */
	protected Connection getConnection(final String id) {
		return system.getConnections().getConnection(id);
	}
	
	/**
	 * Adds an input to the given SCADA connection and connects that input to the given data point. Note that only 
	 * one input can be connected at the same time, otherwise the method will fail with an error. Note that if the data
	 * point does not already exists, it will be created on the fly by this method.
	 * 
	 * @param connection				The connection to which the input has to be added.
	 * @param dataPointType				Type of the target data point.
	 * @param dataPointId				ID if the data point to use as target for the input.
	 * @param configuration				Input configuration parameters.
	 * @throws ConfigurationException	If the configuration is incomplete or invalid or the data point is already in
	 * 									use.
	 * @throws DuplicateIdException		If there exists a data point with the same ID but another type.
	 */
	protected void addInput(final Connection connection, final DataPointType dataPointType, final String dataPointId, 
			final ConfigurationDictionary configuration) throws ConfigurationException, DuplicateIdException {
		// Connection must be valid!
		if (connection == null) {
			throw new ConfigurationException("Invalid connection reference!");
		}
		
		// Create or get the data point.
		final DataPoint dataPoint = createDataPoint(dataPointType, dataPointId, true);
		
		// Should never happen, but we never know...
		if (dataPoint == null) {
			throw new ConfigurationException("Invalid datapoint \"" + dataPointId + "\"");
		}
		
		// It is an input, so we have to permanently select the data point.
		if (!dataPoint.select(connection)) {
			throw new ConfigurationException("Datapoint \"" + dataPointId + "\" used by multiple connectors as input!");
		}
		
		// Add the input to the connection.
		connection.addInput(dataPoint, configuration);
	}
	
	/**
	 * Adds an output to the given SCADA connection and connects that output to the given data point. Note that 
	 * multiple outputs can be connected at the same time. If the data point does not already exists, it will be 
	 * created on the fly by this method.
	 * 
	 * @param connection				The connection to which the output has to be added.
	 * @param dataPointType				Type of the source data point.
	 * @param dataPointId				ID if the data point to use as source for the output.
	 * @param configuration				Output configuration parameters.
	 * @throws ConfigurationException	If the configuration is incomplete or invalid.
	 * @throws DuplicateIdException		If there exists a data point with the same ID but another type.
	 */
	protected void addOutput(final Connection connection, final DataPointType dataPointType, final String dataPointId,
			final ConfigurationDictionary configuration) throws ConfigurationException, DuplicateIdException {
		// Connection must be valid!
		if (connection == null) {
			throw new ConfigurationException("Invalid connector reference!");
		}
		
		// Create or get the data point.
		final DataPoint dataPoint = createDataPoint(dataPointType, dataPointId, true);
		
		// Should never happen, but we never know...
		if (dataPoint == null) {
			throw new ConfigurationException("Invalid datapoint \"" + dataPointId + "\"");
		}
		
		// Add the output to the connector.
		connection.addOutput(dataPoint, configuration);
	}
	
	/**
	 * Main entry point in order to load a standalone jSCADA system.
	 * @param args	Arguments passed to the SCADA system factory.
	 */
	public static void main(String... args) {
		try {
			// Load the system using the program arguments.
			ScadaSystem system = load(args);

			if (system != null) {
				// Start the SCADA system.
				system.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ScadaSystem system;
}
