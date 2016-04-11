package ch.hevs.jscada.model;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * The base class of each data point in the SCADA process model.
 * <br><br>
 * Additionally this base class disposes the central repository where every data point is registered. There is no need
 * to register any data point manually, this is done automatically in the constructor for you.
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public abstract class DataPoint implements Selectable, StringConvertible, DoubleConvertible, Comparable<DataPoint> {
    // Data point identification.
    private final String id;

    // Data point listeners.
    private final List<DataPointListener> updateListeners = new ArrayList<>();
    private final List<DataPointListener> changeListeners = new ArrayList<>();

    // Operate locking using select before operate principle.
    private WeakReference<Object> selectOwner = null;

    /**
     * Constructor, creates a data point with the given ID and registers the data point within the global data point
     * registry.
     *
     * @param id      ID of the data point. Note that the ID should be unique across a system
     * @param process SCADA process to which the data point has to be added.
     * @throws DuplicateIdException
     */
    protected DataPoint(final String id, final Process process) throws DuplicateIdException {
        // Save ID.
        this.id = id;

        // Register data point within the SCADA process.
        if (process != null) {
            process.addDataPoint(this);
        }
    }

    /**
     * Returns the actual type of the data point.
     *
     * @return Data point type.
     */
    public abstract DataPointType getType();

    /**
     * Returns the ID of the data point. The ID can be any string, but it has to be unique.
     *
     * @return Data point's ID.
     */
    public final String getId() {
        return id;
    }

    /**
     * Adds a listener to the data point. If onlyOnChanges is false, the listener will be informed about each update
     * of the data point. Update means that the value has been updated, but not necessarily that the value has changed.
     * If onlyOnChanges is true, the data listener is informed only if the value actually changes.
     *
     * @param listener      Reference to the listener.
     * @param onlyOnChanges If true, the listener is informed only if the data point's value effectively changes.
     */
    public final void addListener(final DataPointListener listener, final boolean onlyOnChanges) {
        // TODO: can we check the type of the listener here?
        if (listener != null) {
            // Depending if the listener is added for updates or changes, add it to the respective list.
            if (onlyOnChanges) {
                changeListeners.add(listener);
            } else {
                updateListeners.add(listener);
            }
        }
    }

    /**
     * Removes the given listener from the list of listeners for the data point.
     *
     * @param listener Reference to the listener.
     */
    public final void removeListener(final DataPointListener listener) {
        if (listener != null) {
            // Remove the listener from booth lists.
            changeListeners.remove(listener);
            updateListeners.remove(listener);
        }
    }

    /**
     * Checks that the reference to the object passed as parameter has gained modification rights by selecting the
     * data point using the method {@link DataPoint#select(Object)}. Throws an exception if not.
     *
     * @param owner The owner of the data point (the object that called {@link DataPoint#select(Object)} before).
     * @throws SelectException This exception is thrown if the data point was not reserved for modification using the
     *                         {@link DataPoint#select(Object)} method before by the calling object.
     */
    protected final void ensureSelectedBy(final Object owner) throws SelectException {
        if (selectOwner == null || selectOwner.get() != owner) {
            throw new SelectException(String.format("Datapoint \"%s\" is not selected by object %s!", id,
                String.valueOf(owner)));
        }
    }

    /**
     * Informs all listeners about the change of the data point's value.
     *
     * @param changed Set to true to indicate that the value has not only updated, it has changed too.
     */
    protected final void update(final boolean changed) {
        for (final DataPointListener listener : updateListeners) {
            // TODO: can we check the type of the listener here?
            listener.dataPointUpdated(this);
        }
        if (changed) {
            for (final DataPointListener listener : changeListeners) {
                // TODO: can we check the type of the listener here?
                listener.dataPointUpdated(this);
            }
        }
    }

    /***
     * Selectable implementation
     ************************************************************************************/
    @Override
    public final void select(final Object object) throws SelectException {
        if (object == null) {
            throw new SelectException("null can not be used as select object!");
        }
        if (isSelected() && selectOwner.get() != object) {
            throw new SelectException(String.format("Datapoint \"%s\" is already selected by %s", id,
                String.valueOf(selectOwner.get())));
        }
        selectOwner = new WeakReference<>(object);
    }

    @Override
    public boolean isSelected() {
        if (selectOwner != null) {
            if (selectOwner.get() != null) {
                return true;
            } else {
                selectOwner = null;
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public final void deselect(final Object object) {
        if (selectOwner != null && selectOwner.get() == object) {
            selectOwner = null;
        }
    }

    /***
     * Comparable implementation
     ************************************************************************************/
    @Override
    public int compareTo(DataPoint other) {
        if (other != null) {
            return id.compareTo(other.id);
        } else {
            return 1;
        }
    }
}
