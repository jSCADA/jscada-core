package ch.hevs.jscada.io.util;

import ch.hevs.jscada.ScadaSystem;
import ch.hevs.jscada.config.ConfigurationDictionary;
import ch.hevs.jscada.config.ConfigurationException;
import ch.hevs.jscada.io.*;
import ch.hevs.jscada.model.DataPoint;
import ch.hevs.jscada.model.DataPointListener;
import ch.hevs.jscada.model.Process;
import ch.hevs.jscada.model.ProcessListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataPointLogger extends AbstractConnection {
	private static final Logger log = LoggerFactory.getLogger(DataPointLogger.class);

	private ProcessListener processListener = new ProcessListener() {
		@Override
		public void dataPointAdded(Process process, DataPoint dataPoint) {
			dataPoint.addListener(dataPointListener, false);
		}
	};

	private DataPointListener dataPointListener = new DataPointListener() {
		@Override
		public void dataPointUpdated(DataPoint dataPoint) {
			log.info("{} = {}", dataPoint.getId(), dataPoint.getStringValue());
		}
	};

	@Override
	public void initialize(final ConfigurationDictionary configuration, final ScadaSystem scadaSystem)
		throws ConfigurationException, ConnectionInitializeException {
		for (DataPoint dataPoint: scadaSystem.getProcess().getDataPoints()) {
			dataPoint.addListener(dataPointListener, false);
		}
		scadaSystem.getProcess().addProcessListener(processListener);
		setState(ConnectionState.CONNECTED);
	}

	@Override
	public void deinitialize(final ScadaSystem scadaSystem) {
		scadaSystem.getProcess().removeListenerFromAllDataPoints(dataPointListener);
		setState(ConnectionState.IDLE);
	}
}
