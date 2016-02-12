package ch.hevs.jscada.config;

import ch.hevs.jscada.exception.ConfigurationException;
import ch.hevs.jscada.exception.ConnectionInitializeException;
import ch.hevs.jscada.exception.DuplicateIdException;
import ch.hevs.jscada.io.Connection;
import ch.hevs.jscada.model.DataPointType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

class XmlScadaSystemFactory extends ScadaSystemFactory {
	// Some minimal XML validity checking...
	private enum ParseState { IDLE, FIELD, CONNECTIONS, INPUTS, OUTPUTS, TRIGGERS }
	
	private class Handler extends DefaultHandler {
		private void parseField(Attributes attributes) throws SAXException {
			for (int i = 0; i < attributes.getLength(); ++i) {
				// Search for "synchronizeInterval" attribute.
				if (attributes.getLocalName(i).equals("synchronizeInterval")) {
					// Parse the value and set the interval on the SCADA system.
					int interval = Integer.parseInt(attributes.getValue(i));
					setSynchronisationInterval(interval);
				} 
			}
		}
		
		private void parseConnection(Attributes attributes) throws SAXException {
			// Get the mandatory class attribute.
			final String clazz = attributes.getValue("", "class");
			if (clazz == null) {
				throw new SAXException("Missing mandatory attribute \"class\" for element \"connector\"");
			}
			
			// Get the mandatory class attribute.
			final String id = attributes.getValue("", "id");
			if (id == null) {
				throw new SAXException("Missing mandatory attribute \"id\" for element \"connector\"");
			}
			
			// Create the connector configuration.
			ConfigurationDictionary configuration = new ConfigurationDictionary();
			for (int i = 0; i < attributes.getLength(); ++i) {
				final String key = attributes.getLocalName(i);
				final String value = attributes.getValue(i);
				if (!key.equals("class") && !key.equals("id")) {
					configuration.set(key, value);
				}
			}
			
			try {
				// Try to create the connection with the given configuration.
				createConnection(clazz, id, configuration);
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | DuplicateIdException
					| ConfigurationException
					| ConnectionInitializeException e) {
				throw new SAXException(e);
			}
		}
		
		private void parseInput(Attributes attributes) throws SAXException {
			// Get the mandatory type attribute.
			final DataPointType type = DataPointType.fromString(attributes.getValue("", "type"));
			if (type == DataPointType.INVALID) {
				throw new SAXException("Missing or invalid mandatory attribute \"type\" for element \"input\"");
			}
			
			// Get the mandatory connectorRef attribute and check for connector existence.
			Connection connection;
			final String connectionId = attributes.getValue("", "connectionRef");
			if (connectionId == null) {
				throw new SAXException("Missing mandatory attribute \"connectionRef\" for element \"input\"");
			}
			connection = getConnection(connectionId);
			if (connection == null) {
				throw new SAXException("ConnectionRef \"" + connectionId + "\" refers to a non existent connection");
			}
			
			// Get the mandatory pointRef and the optional groupRef and nodeRef attributes.
			String dataPointId = "";
			
			final String groupRef = attributes.getValue("", "groupRef");
			if (groupRef != null) {
				dataPointId += groupRef + ".";
			}
			
			final String nodeRef = attributes.getValue("", "nodeRef");
			if (nodeRef != null) {
				dataPointId += nodeRef + ".";
			}
			
			final String pointRef = attributes.getValue("", "pointRef");
			if (pointRef == null) {
				throw new SAXException("Missing mandatory attribute \"pointRef\" for element \"input\"");
			}
			dataPointId += pointRef;
			
			// Create the input configuration.
			ConfigurationDictionary configuration = new ConfigurationDictionary();
			for (int i = 0; i < attributes.getLength(); ++i) {
				final String key = attributes.getLocalName(i);
				final String value = attributes.getValue(i);
				if (!key.equals("type") && !key.equals("connectionRef") && !key.equals("groupRef") && 
						!key.equals("nodeRef") && !key.equals("pointRef")) {
					configuration.set(key, value);
				}
			}

			try {
				// Try to add the input.
				addInput(connection, type, dataPointId, configuration);
			} catch (ConfigurationException | DuplicateIdException e) {
				throw new SAXException(e);
			}
		} 
		
		private void parseOutput(Attributes attributes) throws SAXException {
			// Get the mandatory type attribute.
			final DataPointType type = DataPointType.fromString(attributes.getValue("", "type"));
			if (type == DataPointType.INVALID) {
				throw new SAXException("Missing or invalid mandatory attribute \"type\" for element \"ouput\"");
			}
			
			// Get the mandatory connectorRef attribute and check for connector existence.
			Connection connection;
			final String connectionId = attributes.getValue("", "connectionRef");
			if (connectionId == null) {
				throw new SAXException("Missing mandatory attribute \"connectionRef\" for element \"input\"");
			}
			connection = getConnection(connectionId);
			if (connection == null) {
				throw new SAXException("ConnectionRef \"" + connectionId + "\" refers to a non existent connection");
			}
			
			// Get the mandatory pointRef and the optional groupRef and nodeRef attributes.
			String dataPointId = "";
			
			final String groupRef = attributes.getValue("", "groupRef");
			if (groupRef != null) {
				dataPointId += groupRef + ".";
			}
			
			final String nodeRef = attributes.getValue("", "nodeRef");
			if (nodeRef != null) {
				dataPointId += nodeRef + ".";
			}
			
			final String pointRef = attributes.getValue("", "pointRef");
			if (pointRef == null) {
				throw new SAXException("Missing mandatory attribute \"pointRef\" for element \"input\"");
			}
			dataPointId += pointRef;
			
			// Create the output configuration.
			ConfigurationDictionary configuration = new ConfigurationDictionary();
			for (int i = 0; i < attributes.getLength(); ++i) {
				final String key = attributes.getLocalName(i);
				final String value = attributes.getValue(i);
				if (!key.equals("type") && !key.equals("connectorRef") && !key.equals("groupRef") && 
						!key.equals("nodeRef") && !key.equals("pointRef")) {
					configuration.set(key, value);
				}
			}
		
			try {
				// Try to add the output.
				addOutput(connection, type, dataPointId, configuration);
			} catch (ConfigurationException | DuplicateIdException e) {
				throw new SAXException(e);
			}
		}
		
		void parseTrigger(Attributes attributes) throws SAXException {
			// TODO: Implement. (Maybe we should re-think the whole alarm stuff!)
			throw new SAXException("Not implemented!");
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) 
				throws SAXException {
			if (state == ParseState.IDLE) {
				if (qName.equals("field")) {
					state = ParseState.FIELD;
					parseField(attributes);
				}
			} else if (state == ParseState.FIELD && qName.equals("connections")) {
				state = ParseState.CONNECTIONS;
			} else if (state == ParseState.CONNECTIONS && qName.equals("connection")) {
				parseConnection(attributes);
			} else if (state == ParseState.FIELD && qName.equals("outputs")) {
				state = ParseState.OUTPUTS;
			} else if (state == ParseState.OUTPUTS && qName.equals("output")) {
				parseOutput(attributes);
			} else if (state == ParseState.FIELD && qName.equals("inputs")) {
				state = ParseState.INPUTS;
			} else if (state == ParseState.INPUTS && qName.equals("input")) {
				parseInput(attributes);
			} else if (state == ParseState.FIELD && qName.equals("triggers")) {
				state = ParseState.TRIGGERS;
			} else if (state == ParseState.TRIGGERS && qName.equals("trigger")) {
				parseTrigger(attributes);
			} else {
				throw new SAXException("Invalid tag \"" + qName + "\" found.");
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			switch (state) {
				case IDLE:
					break;

				case FIELD:
					if (qName.equals("field")) state = ParseState.IDLE;
					break;
					
				case CONNECTIONS:
					if (qName.equals("connections")) state = ParseState.FIELD;
					break;
					
				case INPUTS:
					if (qName.equals("inputs")) state = ParseState.FIELD;
					break;
					
				case OUTPUTS:
					if (qName.equals("outputs")) state = ParseState.FIELD;
					break;
					
				case TRIGGERS:
					if (qName.equals("triggers")) state = ParseState.TRIGGERS;
					break;
					
				default:
					break;
			
			}
		}
		
		ParseState state = ParseState.IDLE;
	}
	
	@Override
	protected void loadImplementation(ConfigurationDictionary configuration) throws Exception {
		// Construct the filename using the mandatory parameter "file".
		String filename = configuration.get("file", String.class);
		
		// Parse the XML file.
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();
		saxParser.parse(filename, new Handler());
	}
}
