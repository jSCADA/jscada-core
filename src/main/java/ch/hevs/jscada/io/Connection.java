package ch.hevs.jscada.io;

import ch.hevs.jscada.ScadaSystem;
import ch.hevs.jscada.config.ConfigurationDictionary;
import ch.hevs.jscada.config.ConfigurationException;

public interface Connection {
    /**
     * Initializes the connection using the provided configuration parameters.
     *
     * @param configuration The configuration to use in order to initialize the connection.
     * @param scadaSystem   The SCADA system the connection is added to.
     * @throws ConfigurationException        Thrown if a configuration parameter is missing or invalid.
     * @throws ConnectionInitializeException Thrown if the initialization was not successful.
     */
    void initialize(ConfigurationDictionary configuration, ScadaSystem scadaSystem)
        throws ConfigurationException, ConnectionInitializeException;

    /**
     * Closes the connection and switches to mode {@link ConnectionState#IDLE}.
     *
     * @param scadaSystem The SCADA system the connection is removed from.
     */
    void deinitialize(ScadaSystem scadaSystem);

    /**
     * Returns the actual state of the connection.
     *
     * @return FieldConnection state.
     */
    ConnectionState getConnectionState();

    /**
     * Adds a connection listener in order to inform the object about changes of the connection mode.
     *
     * @param listener Listener to add.
     */
    void addConnectionListener(ConnectionListener listener);

    /**
     * Removes the given connection listener.
     *
     * @param listener Listener to remove.
     */
    void removeConnectionListener(ConnectionListener listener);
}
