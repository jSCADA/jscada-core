package ch.hevs.jscada.io;

import ch.hevs.jscada.exception.DuplicateIdException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Groups multiple connections into one single group of connections and offers methods in order to execute common tasks
 * on all of the contained connections.
 * 
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public class ConnectionGroup implements Synchronizable {
	/**
	 * Returns the connection with the given ID if the connection exists, null otherwise.
	 * 
	 * @param id	ID of the connection to return.
	 * @return		Connection attributes to the given ID or null if not connection with the given ID exists.
	 */
    public Connection getConnection(final String id) {
    	return connections.get(id);
    }

    /**
     * Adds the given connection to the connection group and assigns the connection the given ID. Note that the ID does
     * not have to be 
     *  
     * @param id					ID to assign to the connection inside the group.
     * @param connection			The connection to add to the group.
     * @throws DuplicateIdException	If a connection with the ID attributed already exists.
     */
	public void addConnection(final String id, final Connection connection) throws DuplicateIdException {
		// Check for duplicate ID.
    	if (connections.containsKey(id)) {
    		throw new DuplicateIdException(id);
    	}
    	
    	// Register the connection.
        connections.put(id, connection);
        
        // Add the group as connection listener.
        connection.addConnectionListener(connectionListener);
	}
	
	/**
	 * Removes the connection with the given ID.
	 * 
	 * @param id	ID attributed to the connection to remove.
	 */
	public void removeConnection(final String id) {
		Connection connection = connections.get(id);
		if (connection != null) {
			connection.removeConnectionListener(connectionListener);
			connections.remove(id);
		}
	}
	
	/**
	 * Removes the given connection.
	 * 
	 * @param connection	Connection to be removed.
	 */
	public void removeConnection(final Connection connection) {
		for (Map.Entry<String,Connection> entry: connections.entrySet()) {
			if (entry.getValue() == connection) {
				entry.getValue().removeConnectionListener(connectionListener);
				connections.remove(entry.getKey());
				return;
			} 
		}
	}

	/**
	 * Returns the number of connections that are in the group.
	 * @return Number of connections.
     */
	public int connectionCount() {
		return connections.size();
	}

	/**
	 * Stops all SCADA connections.
	 */
	public void deinitializeConnections() {
		for (Connection connection: connections.values()) {
			connection.deinitialize();
		}
	}

	/**
	 * Adds the given ConnectionListener and notifies the listener about events on all connections.
	 * 
	 * @param listener	Connection listener to add.
	 */
	public void addConnectionListener(ConnectionListener listener) {
		connectionListeners.add(listener);
	}
	
	/**
	 * Removes the given ConnectionListener.
	 * 
	 * @param listener	Connection listener to remove.
	 */
	public void removeConnectionListener(ConnectionListener listener) {
		connectionListeners.remove(listener);
	}


	/*** Synchronizable implementation ********************************************************************************/
	@Override
	public void synchronize() {
		// Notify all synchronization listeners that we start synchronizing.
		for (SynchronizableListener listener: listeners) {
			listener.willSynchronize(this);
		}
		
		// Synchronize all connections using a thread pool to paralyze the work - normally a connection has to wait
		// most of the time for responses from the peer and during this time we can handle other connections.
		ExecutorService exec = Executors.newCachedThreadPool();
    	for (final Connection connection: connections.values()) {
    		// If the connection is event based only, we do not need to synchronize.
    		if (connection.getMode() != ConnectionMode.EVENT_BASED) {
    			// Submit connection synchronization to the thread pool.
    			exec.submit(new Runnable() {
					@Override
					public void run() {
						connection.synchronize();
					}
    			});
    		}
    	}
    	
    	// Isolate running threads and disallow new ones.
    	exec.shutdown();
    	try {
    		// Wait for all the connection synchronization methods to finish.
    		// TODO: Limiting to a reasonable time would actually make sense here...
			exec.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	// Inform all listeners that the synchronization took place.
    	for (SynchronizableListener listener: listeners) {
			listener.didSynchronize(this);
		}
    }
	
    @Override
	public void addListener(SynchronizableListener listener) {
    	if (listener != null) {
    		listeners.add(listener);
    	}
	}

	@Override
	public void removeListener(SynchronizableListener listener) {
		listeners.remove(listener);
	}
		
	// Global connections registry.
    private final Map<String,Connection> connections = new TreeMap<>();

    // Synchronizable listeners.
    private final List<SynchronizableListener> listeners = new LinkedList<>();
    
    // ConnectionListener implementation.
    private final ConnectionListener connectionListener = new ConnectionListener() {
		@Override
		public void connectionThrownException(Connection connection, Exception exception) {
			for (ConnectionListener listener: connectionListeners) {
				listener.connectionThrownException(connection, exception);
			}
		}
		
		@Override
		public void connectionStateChanged(Connection connection, ConnectionState state) {
			for (ConnectionListener listener: connectionListeners) {
				listener.connectionStateChanged(connection, state);
			}
		}
	};
	
	private final List<ConnectionListener> connectionListeners = new LinkedList<>();
}
