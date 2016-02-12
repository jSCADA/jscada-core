package ch.hevs.jscada.config;

import ch.hevs.jscada.io.ConnectionGroup;
import ch.hevs.jscada.model.Process;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a complete SCADA system, which can be either created using the jSCADA API or can be loaded from different
 * configuration sources like for example XML files or data bases.
 * 
 * The system consists basically of the process, the actual model of the data and all related connections. A connection
 * is the link between the actual field or monitor system into or out of jSCADA.
 * 
 * @author Michael Clausen (michael.clausen@hevs)
 */
public class ScadaSystem {
	/**
	 * Returns a reference to the SCADA process. The SCADA process hosts all data points - the actual data.
	 * 
	 * @return	SCADA process.
	 */
	public Process getProcess() {
		return process;
	}
	
	/**
	 * Returns a reference to the SCADA system's primary connection group (which should contain ALL connections related
	 * to the SCADA system.
	 * 
	 * @return	Connection group containing all connections.
	 */
	public ConnectionGroup getConnections() {
		return connections;
	}
	
	/**
	 * Returns the actual synchronization interval used by the SCADA system in order to poll connections which do not 
	 * support event based communication. A value of 0 means no synchronization at all (this may cause some connections
	 * not to work).
	 * 
	 * @return	Synchronization interval in milliseconds.
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
	 * @param intervalMs	Synchronization interval in milliseconds.
	 */
	public void setSynchronizationInterval(final int intervalMs) {
		boolean wasActive = isActive();
		if (wasActive) {
			stop();
		}
		synchronizeInterval = intervalMs;
		if (wasActive) {
			start();
		}
	}
	
	/**
	 * Starts the SCADA system. This means that all connections are synchronized using the configured synchronization 
	 * interval. You can synchronize the connections manually by calling getConnexctions().synchronize(), if you have
	 * special constraints.
	 */
	public void start() {
		// If timer is not actually running...
		if (!isActive() && synchronizeInterval != 0) {
			// Start a new timer that synchronizes all connections (the group).
			synchronizationTimer = new Timer();
			synchronizationTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					connections.synchronize();
				}
			}, 0, synchronizeInterval);
		}
	}
	
	/**
	 * Returns true if synchronization is active.
	 * 
	 * @return	True if synchronization is active.
	 */
	public boolean isActive() {
		return synchronizationTimer != null;
	}
	
	/**
	 * Stops the synchronization of the SCADA system. Note that the system remains valid and the synchronization can be
	 * restarted at any time using the start() method.
	 */
	public void stop() {
		if (synchronizationTimer != null) {
			synchronizationTimer.cancel();
			synchronizationTimer = null;
		}
		connections.deinitializeConnections();
	}
	
	private int synchronizeInterval = 60000;
	private Timer synchronizationTimer = null;
	private final Process process = new Process();
	private final ConnectionGroup connections = new ConnectionGroup();
}
