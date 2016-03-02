package ch.hevs.jscada.io.field;

import ch.hevs.jscada.config.ConfigurationDictionary;
import ch.hevs.jscada.config.ConfigurationException;
import ch.hevs.jscada.io.*;
import ch.hevs.jscada.model.DataPoint;

import java.util.List;

/**
 * A field connection connects a given field technology or protocol to jSCADA. Any connection should implement this
 * protocol in order to be compatible with jSCADA and in order to be configurable via the different configuration
 * options that are provided by jSCADA (Example: XML file).
 *  
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
public interface FieldConnection extends Connection, Synchronizable {
	/**
	 * Adds a permanent input to the connection. An input transfers the value from the connected device/system to the 
	 * jSCADA data model (data point). It is quite important to understand, that only one input at the time can be 
	 * attached to a single data point, as if multiple sources would update the same data point, race conditions will 
	 * arise. So jSCADA ensures that the data point has an exclusive writer by applying the select before operate 
	 * semantic, whereas the data point has to be selected before it can actually manipulated. If another already 
	 * selected the data, the selection will fail. If you are adding an input, you need to know that the data point
	 * assigned to the input will be permanently selected and you will not be able to update the data point from another
	 * source.
	 * 
	 * @param dataPoint					The data point to update using the input value from the connection.
	 * @param inputConfiguration		The configuration of the input, this can be for example data type or address...
	 * @throws ConfigurationException	If a configuration parameter for the input is missing or invalid.
	 */
	void addInput(DataPoint dataPoint, ConfigurationDictionary inputConfiguration) throws ConfigurationException;
	
	/**
	 * Adds an output to the connection. An output transfers the value from the jSCADA model (data point) to the 
	 * output of the connected device or system every time the value of the data point has changed.
	 * 
	 * @param dataPoint					The data point that serves as source for the connection's output.
	 * @param outputConfiguration		The configuration of the output, this can be for example data type or address...
	 * @throws ConfigurationException	If a configuration parameter for the output is missing or invalid.
	 */
	void addOutput(DataPoint dataPoint, ConfigurationDictionary outputConfiguration) throws ConfigurationException;
	
	/**
	 * Returns a list of modes supported by the connection. Note that apart that a connection needs to support at 
	 * least one of the modes, some connection implementations might support multiple modes.
	 * 
	 * @return	List of modes supported by the connection.
	 */
	List<FieldConnectionMode> supportedModes();
	
	/**
	 * Returns the actual mode the connection operates with.
	 * 
	 * @return	FieldConnection mode.
	 */
	FieldConnectionMode getMode();
	
	/**
	 * Tries to change the mode of the connection to the given mode. Note that if the connection does not support the 
	 * new mode, a ConfigurationException will be thrown.
	 * 
	 * @param mode						FieldConnection mode to set.
	 * @throws ConfigurationException	If the connection does not support the given mode.
	 */
	void setMode(FieldConnectionMode mode) throws ConfigurationException;
}
