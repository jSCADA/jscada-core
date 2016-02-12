package ch.hevs.jscada.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hevs.jscada.config.ScadaSystem;
import ch.hevs.jscada.config.ScadaSystemFactory;
import ch.hevs.jscada.model.DataPoint;
import ch.hevs.jscada.model.DataPointListener;

public class LogDataPointListener implements DataPointListener {
	@Override
	public void dataPointUpdated(DataPoint dataPoint) {
		log.info("{} = {}", dataPoint.getId(), dataPoint.getStringValue());
	}

	public static void main(String... args) {
		
        
		try {
			LogDataPointListener listener = new LogDataPointListener();
			
			// Load the system using the program arguments.
			ScadaSystem system = ScadaSystemFactory.load(args);

			if (system != null) {
				// Add the listener to all data points.
				for (DataPoint dp : system.getProcess().getDataPoints()) {
					dp.addListener(listener, false);
				}

				// Start the SCADA system.
				system.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*** Private methods and attributes ******************************************************************************/
	private static final Logger log = LoggerFactory.getLogger(LogDataPointListener.class);
}
