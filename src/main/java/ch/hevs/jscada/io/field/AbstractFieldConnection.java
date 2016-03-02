package ch.hevs.jscada.io.field;

import ch.hevs.jscada.io.AbstractConnection;
import ch.hevs.jscada.io.SynchronizableListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFieldConnection extends AbstractConnection implements FieldConnection {
    private final List<SynchronizableListener> synchronizableListeners = new ArrayList<>();

    @Override
    public final void addSynchronizableListener(final SynchronizableListener listener) {
        synchronizableListeners.add(listener);
    }

    @Override
    public final void removeSynchronizableListener(final SynchronizableListener listener) {
        synchronizableListeners.remove(listener);
    }

    protected void willSynchronize() {
        for (final SynchronizableListener listener: synchronizableListeners) {
            listener.willSynchronize(this);
        }
    }

    protected final void didSynchronize() {
        for (final SynchronizableListener listener: synchronizableListeners) {
            listener.didSynchronize(this);
        }
    }
}
