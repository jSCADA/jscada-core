package ch.hevs.jscada.io;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract connection class that helps classes implementing the Connection interface by automatically publishing the
 * connection state upon of a change to all subscribed listeners. All a class that extends this has to do in order to
 * publish its new state to all listeners is to call the method {@link AbstractConnection#setState(ConnectionState)}
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public abstract class AbstractConnection implements Connection {
    private final List<ConnectionListener> listeners = new LinkedList<>();
    private ConnectionState state = ConnectionState.IDLE;

    @Override
    public final ConnectionState getConnectionState() {
        return state;
    }

    @Override
    public final void addConnectionListener(final ConnectionListener listener) {
        listeners.add(listener);
    }

    @Override
    public final void removeConnectionListener(final ConnectionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Sets the state to the given value and notifies all listeners if the state has changed.
     *
     * @param state New state.
     */
    protected final void setState(final ConnectionState state) {
        final boolean changed = this.state != state;
        this.state = state;
        if (changed) {
            for (final ConnectionListener listener : listeners) {
                listener.connectionStateChanged(this, this.state);
            }
        }
    }

    /**
     * Notifies all connection listeners about the exception thrown by the connection.
     *
     * @param exception The exception thrown.
     */
    protected final void notifyAboutException(final Exception exception) {
        for (final ConnectionListener listener : listeners) {
            listener.connectionThrownException(this, exception);
        }
    }
}
