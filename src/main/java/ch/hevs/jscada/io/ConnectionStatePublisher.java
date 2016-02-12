package ch.hevs.jscada.io;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple helper class that helps Connections to automatically publish their connection state upon of a change to 
 * all subscribed listeners.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public class ConnectionStatePublisher {
	/**
	 * Constructs the publisher for the given connection.
	 * 
	 * @param connection	Connection, should never be null.
	 */
	public ConnectionStatePublisher(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Returns the actual state saved.
	 * 
	 * @return	Actual state.
	 */
	public ConnectionState getState() {
		return state;
	}
	
	/**
	 * Sets the state to the given value and notifies all listeners if the state has changed.
	 * 
	 * @param state	New state.
	 */
	public void setState(ConnectionState state) {
		boolean changed = !this.state.equals(state);
		this.state = state;
		if (changed) {
			for (ConnectionListener listener: listeners) {
				listener.connectionStateChanged(connection, this.state);
			}
		}
	}
	
	/**
	 * Notifies all connection listeners about the exception thrown by the connection.
	 * 
	 * @param exception	The exception thrown.
	 */
	public void notifyAboutException(final Exception exception) {
		for (ConnectionListener listener: listeners) {
			listener.connectionThrownException(connection, exception);
		}
	}
	
	/**
	 * Adds the given connection listener.
	 * 
	 * @param listener	Connection listener to add.
	 */
	public void add(ConnectionListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes the given connection listener.
	 * 
	 * @param listener	Connection listener to remove.
	 */
	public void remove(ConnectionListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Returns all registered connection listeners.
	 * 
	 * @return	Connection listeners.
	 */
	public Collection<ConnectionListener> getListeners() {
		return listeners;
	}
	
	private final List<ConnectionListener> listeners = new LinkedList<>();
	private ConnectionState state = ConnectionState.IDLE;
	private final Connection connection;
}
