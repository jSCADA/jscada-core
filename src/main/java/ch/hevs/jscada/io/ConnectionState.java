package ch.hevs.jscada.io;

/**
 * Defines the different states a connection can be in.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public enum ConnectionState {
    /**
     * The connection is idle. This means that the connection is not supposed to be connected to a remote SCADA system
     * and thus it does nothing.
     */
    IDLE,

    /**
     * The connection is unconnected. This means that the connection is active but does not have actually a connection
     * to the remote system. Network problems or other issues may cause this state.
     */
    DISCONNECTED,

    /**
     * The connection is connected and can be used in order to get or set values from/to the target system.
     */
    CONNECTED
}
