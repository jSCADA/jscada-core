package ch.hevs.jscada.io;

/**
 * Interface for connection listeners. Basically a connection listener gets informed about the vital state of a 
 * connection during it's life cycle.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public interface ConnectionListener {
	/**
	 * The connection state has changed.
	 * 
	 * @param connection	The connection for which the state has changed.
	 * @param state			The new connection state.
	 */
	void connectionStateChanged(final Connection connection, final ConnectionState state);
	
	void connectionThrownException(final Connection connection, final Exception exception);
}
