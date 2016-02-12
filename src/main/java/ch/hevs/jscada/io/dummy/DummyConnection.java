package ch.hevs.jscada.io.dummy;

import ch.hevs.jscada.config.ConfigurationDictionary;
import ch.hevs.jscada.exception.ConfigurationException;
import ch.hevs.jscada.exception.ConnectionInitializeException;
import ch.hevs.jscada.exception.ConversionException;
import ch.hevs.jscada.io.*;
import ch.hevs.jscada.model.DataPoint;
import ch.hevs.jscada.model.DataPointListener;

import java.util.*;

public class DummyConnection implements Connection, DataPointListener {
	private final Map<String,DataPoint> inputs = new TreeMap<>();
	private final Map<DataPoint,String> outputs = new TreeMap<>();
	private final Map<String,String> values = new TreeMap<>();
	private final List<SynchronizableListener> syncListeners = new ArrayList<>();
	private final ConnectionStatePublisher state = new ConnectionStatePublisher(this);
	
	@Override
	public void initialize(ConfigurationDictionary configuration)
			throws ConfigurationException, ConnectionInitializeException {
		state.setState(ConnectionState.CONNECTED);
	}
	
	@Override
	public void deinitialize() {
		state.setState(ConnectionState.IDLE);
		values.clear();
	}
	
	@Override
	public void synchronize() {
		for (SynchronizableListener listener: syncListeners) {
			listener.willSynchronize(this);
		}
		
		for (String key: inputs.keySet()) {
			DataPoint dataPoint = inputs.get(key);
			try {
				String value = values.get(key);
				if(value == null) {
				  value = "0";
				}
			  dataPoint.setStringValue(value);
			} catch (ConversionException e) {
				e.printStackTrace();
			}
		}
		
		for (SynchronizableListener listener: syncListeners) {
			listener.didSynchronize(this);
		}
	}

	@Override
	public void addListener(SynchronizableListener listener) {
		syncListeners.add(listener);
	}

	@Override
	public void removeListener(SynchronizableListener listener) {
		syncListeners.remove(listener);
	}

	@Override
	public void addInput(DataPoint dataPoint, ConfigurationDictionary inputConfiguration)
			throws ConfigurationException {
		String id = inputConfiguration.get("id", String.class);
		inputs.put(id, dataPoint);
	}

	@Override
	public void addOutput(DataPoint dataPoint, ConfigurationDictionary outputConfiguration)
			throws ConfigurationException {
		String id = outputConfiguration.get("id", String.class);
		outputs.put(dataPoint, id);
		dataPoint.addListener(this, false);
	}

	@Override
	public List<ConnectionMode> supportedModes() {
		return Collections.singletonList(ConnectionMode.SYNCHRONEOUS_INPUTS);
	}

	@Override
	public ConnectionMode getMode() {
		return ConnectionMode.SYNCHRONEOUS_INPUTS;
	}

	@Override
	public void setMode(ConnectionMode mode) throws ConfigurationException {
		if (mode != ConnectionMode.SYNCHRONEOUS_INPUTS) {
			throw new ConfigurationException("Mode not supported!");
		}
	}

	@Override
	public ConnectionState getConnectionState() {
		return ConnectionState.CONNECTED;
	}

	@Override
	public void addConnectionListener(ConnectionListener listener) {
	}

	@Override
	public void removeConnectionListener(ConnectionListener listener) {
	}

	@Override
	public void dataPointUpdated(DataPoint dataPoint) {
		String id = outputs.get(dataPoint);
		if (id != null) {
			values.put(id, dataPoint.getStringValue());
		}
	}
}
