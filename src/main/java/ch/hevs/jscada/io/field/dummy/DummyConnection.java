package ch.hevs.jscada.io.field.dummy;

import ch.hevs.jscada.ScadaSystem;
import ch.hevs.jscada.config.ConfigurationDictionary;
import ch.hevs.jscada.config.ConfigurationException;
import ch.hevs.jscada.io.ConnectionInitializeException;
import ch.hevs.jscada.model.ConversionException;
import ch.hevs.jscada.io.ConnectionState;
import ch.hevs.jscada.io.SynchronizableListener;
import ch.hevs.jscada.io.field.AbstractFieldConnection;
import ch.hevs.jscada.io.field.FieldConnectionMode;
import ch.hevs.jscada.model.DataPoint;
import ch.hevs.jscada.model.DataPointListener;
import ch.hevs.jscada.model.SelectException;

import java.util.*;

public final class DummyConnection extends AbstractFieldConnection implements DataPointListener {
	private final Map<String,DataPoint> inputs = new TreeMap<>();
	private final Map<DataPoint,String> outputs = new TreeMap<>();
	private final Map<String,String> values = new TreeMap<>();
	private final List<SynchronizableListener> syncListeners = new ArrayList<>();

	/*** FieldConnection implementation *******************************************************************************/
	@Override
	public void initialize(final ConfigurationDictionary configuration, final ScadaSystem scadaSystem)
		throws ConfigurationException, ConnectionInitializeException {
		setState(ConnectionState.CONNECTED);
	}
	
	@Override
	public void deinitialize(final ScadaSystem scadaSystem) {
		setState(ConnectionState.IDLE);
		values.clear();
	}

	@Override
	public void addInput(final DataPoint dataPoint, final ConfigurationDictionary inputConfiguration)
		throws ConfigurationException {
		String id = inputConfiguration.get("id", String.class);
		inputs.put(id, dataPoint);
	}

	@Override
	public void addOutput(final DataPoint dataPoint, final ConfigurationDictionary outputConfiguration)
		throws ConfigurationException {
		String id = outputConfiguration.get("id", String.class);
		outputs.put(dataPoint, id);
		dataPoint.addListener(this, false);
	}

	@Override
	public List<FieldConnectionMode> supportedModes() {
		return Collections.singletonList(FieldConnectionMode.SYNCHRONOUS_INPUTS);
	}

	@Override
	public FieldConnectionMode getMode() {
		return FieldConnectionMode.SYNCHRONOUS_INPUTS;
	}

	@Override
	public void setMode(final FieldConnectionMode mode) throws ConfigurationException {
		if (mode != FieldConnectionMode.SYNCHRONOUS_INPUTS) {
			throw new ConfigurationException("Mode not supported!");
		}
	}

	/*** Synchronizable implementation ********************************************************************************/
	@Override
	public void synchronize() {
		willSynchronize();
		
		for (String key: inputs.keySet()) {
			DataPoint dataPoint = inputs.get(key);
			try {
				String value = values.get(key);
				if (value == null) {
					value = "0";
				}
				dataPoint.setStringValue(value, this);
			} catch (ConversionException | SelectException e) {
				notifyAboutException(e);
			}
		}
		
		didSynchronize();
	}

	/*** DataPointListener implementation *****************************************************************************/
	@Override
	public void dataPointUpdated(DataPoint dataPoint) {
		String id = outputs.get(dataPoint);
		if (id != null) {
			values.put(id, dataPoint.getStringValue());
		}
	}
}
