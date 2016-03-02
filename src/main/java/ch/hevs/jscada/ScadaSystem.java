package ch.hevs.jscada;

import ch.hevs.jscada.io.ConnectionGroup;
import ch.hevs.jscada.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a complete SCADA system, which can be either created using the jSCADA API or can be loaded from different
 * configuration sources like for example XML files or data bases.
 * <br><br>
 * The system consists basically of the process - the actual model of the data - and all related connections. A
 * connection is the link between the actual field or monitor system into or out of jSCADA.
 *
 * @author Michael Clausen (michael.clausen@hevs)
 */
public class ScadaSystem {
    private static final Logger log = LoggerFactory.getLogger(ScadaSystem.class);

    public static final int DEFAULT_SYNCHRONIZE_INTERVAL = 60000;
    public static final int NO_SYNCHRONIZATION = 0;

    private int synchronizeInterval = DEFAULT_SYNCHRONIZE_INTERVAL;
    private boolean active = false;
    private Timer synchronizationTimer = null;
    private final Process process = new Process();
    private final ConnectionGroup connections = new ConnectionGroup();

    /**
     * Returns a reference to the SCADA process. The SCADA process hosts all data points - the actual data.
     *
     * @return SCADA process.
     */
    public Process getProcess() {
        return process;
    }

    /**
     * Returns a reference to the SCADA system's primary connection group (which should contain ALL connections related
     * to the SCADA system.
     *
     * @return FieldConnection group containing all connections.
     */
    public ConnectionGroup getConnections() {
        return connections;
    }

    /**
     * Returns the actual synchronization interval used by the SCADA system in order to poll connections which do not
     * support event based communication. A value of 0 means no synchronization at all (this may cause some connections
     * not to work).
     *
     * @return Synchronization interval in milliseconds.
     */
    public int getSynchronizationInterval() {
        return synchronizeInterval;
    }

    /**
     * Sets the synchronization interval used by the SCADA system in order to poll connections which do not support
     * event based communication. A value of 0 means no synchronization at all (this may cause some connections
     * not to work). If you change the value while the SCADA system was active, the synchronization will be stopped and
     * restarted automatically using the new interval.
     *
     * @param intervalMs Synchronization interval in milliseconds.
     */
    public void setSynchronizationInterval(final int intervalMs) {
        boolean wasActive = active;

        // Stop the system if we are active.
        if (wasActive) {
            stop();
        }

        // Set the new synchronization interval.
        synchronizeInterval = intervalMs;

        // Restart the SCADA system if we were active before.
        if (wasActive) {
            start();
        }
    }

    /**
     * Starts the SCADA system. This means that all connections are synchronized using the configured synchronization
     * interval. You can synchronize the connections manually by calling getConnections().synchronize(), if you have
     * special constraints or if you want to synchronize manually.
     */
    public void start() {
        // If timer is not actually running...
        if (!active && synchronizeInterval != 0) {
            log.info("Starting system with synchronization interval {}ms.", synchronizeInterval);

            // Start a new timer that synchronizes all connections (the group).
            synchronizationTimer = new Timer();
            synchronizationTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    log.trace("Starting synchronization step...");
                    connections.synchronize();
                    log.trace("Finished synchronization step.");
                }
            }, 0, synchronizeInterval);
        } else {
            log.info("Starting system without synchronization.");
        }

        // System is now active (even if we would not actually synchronize when interval is 0).
        active = true;
    }

    /**
     * Returns true if synchronization is active.
     *
     * @return True if synchronization is active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Stops the synchronization of the SCADA system. Note that the system remains valid and the synchronization can be
     * restarted at any time using the start() method.
     */
    public void stop() {
        log.info("Stopping SCADA system.");
        if (synchronizationTimer != null) {
            synchronizationTimer.cancel();
            synchronizationTimer = null;
        }
        connections.deinitializeConnections(this);

        active = false;
    }
}
