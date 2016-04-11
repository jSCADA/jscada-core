package ch.hevs.jscada.factory;

import ch.hevs.jscada.config.ConfigurationDictionary;
import ch.hevs.jscada.config.ConfigurationException;
import ch.hevs.jscada.io.Connection;
import ch.hevs.jscada.io.ConnectionInitializeException;
import ch.hevs.jscada.io.field.FieldConnection;
import ch.hevs.jscada.model.DataPointType;
import ch.hevs.jscada.model.DuplicateIdException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Supports loading a jSCADA system definition from a XML file or from a resource inside the jar..
 *
 * @author Michael Clausen (michael.clausen@hevs.ch)
 */
class XmlScadaSystemFactory extends ScadaSystemFactory {
    // Some minimalistic XML validity checking...
    private enum ParseState {
        IDLE, FIELD, CONNECTIONS, INPUTS, OUTPUTS, TRIGGERS
    }

    private ParseState state = ParseState.IDLE;

    private final DefaultHandler handler = new DefaultHandler() {
        private Locator locator;

        private void parseField(ConfigurationDictionary attributes) throws SAXParseException {
            try {
                setSynchronisationInterval(attributes.get("synchronizeInterval", 0));
            } catch (ConfigurationException e) {
                throw new SAXParseException(e.getMessage(), locator, e);
            }
        }

        private void parseConnection(ConfigurationDictionary attributes) throws SAXParseException {
            try {
                // Get the mandatory class attribute.
                final String clazz = attributes.get("class", String.class);

                // Get the mandatory class attribute.
                final String id = attributes.get("id", String.class);

                // Try to create the connection with the given configuration.
                createConnection(clazz, id, attributes);
            } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | DuplicateIdException
                | ConfigurationException
                | ConnectionInitializeException e) {
                throw new SAXParseException(e.getMessage(), locator, e);
            }
        }

        private void parseInput(ConfigurationDictionary attributes) throws SAXParseException {
            try {
                // Get the mandatory type attribute.
                final DataPointType type = attributes.get("type", DataPointType.class);

                // Get the mandatory connectorRef attribute and validate for connector existence.
                final String connectionId = attributes.get("connectionRef", String.class);
                final Connection connection = getConnection(connectionId);
                if (connection == null) {
                    throw new SAXParseException("ConnectionRef \""
                        + connectionId + "\" refers to a non existent fieldConnection", locator);
                }
                if (!(connection instanceof FieldConnection)) {
                    throw new SAXParseException("ConnectionRef \""
                        + connectionId + "\" refers to a connection which is not of type field connection", locator);
                }

                // Get the mandatory pointRef and the optional groupRef and nodeRef attributes.
                String dataPointId = attributes.get("groupRef", "");
                if (!"".equals(dataPointId)) {
                    dataPointId += ".";
                }
                dataPointId += attributes.get("nodeRef", "");
                if (!"".equals(dataPointId) && !dataPointId.endsWith(".")) {
                    dataPointId += ".";
                }
                dataPointId += attributes.get("pointRef", String.class);

                // Try to add the input.
                addInput((FieldConnection) connection, type, dataPointId, attributes);

            } catch (ConfigurationException | DuplicateIdException e) {
                throw new SAXParseException(e.getMessage(), locator, e);
            }
        }

        private void parseOutput(ConfigurationDictionary attributes) throws SAXParseException {
            try {
                // Get the mandatory type attribute.
                final DataPointType type = attributes.get("type", DataPointType.class);

                final String connectionId = attributes.get("connectionRef", String.class);
                Connection connection = getConnection(connectionId);
                if (connection == null) {
                    throw new SAXParseException("ConnectionRef \""
                        + connectionId + "\" refers to a non existent fieldConnection", locator);
                }
                if (!(connection instanceof FieldConnection)) {
                    throw new SAXParseException("ConnectionRef \""
                        + connectionId + "\" refers to a connection which is not of type field connection", locator);
                }

                // Get the mandatory pointRef and the optional groupRef and nodeRef attributes.
                String dataPointId = attributes.get("groupRef", "");
                if (!"".equals(dataPointId)) {
                    dataPointId += ".";
                }
                dataPointId += attributes.get("nodeRef", "");
                if (!"".equals(dataPointId) && !dataPointId.endsWith(".")) {
                    dataPointId += ".";
                }
                dataPointId += attributes.get("pointRef", String.class);

                // Try to add the output.
                addOutput((FieldConnection) connection, type, dataPointId, attributes);
            } catch (ConfigurationException | DuplicateIdException e) {
                throw new SAXParseException(e.getMessage(), locator, e);
            }
        }

        void parseTrigger(ConfigurationDictionary attributes) throws SAXParseException {
            // TODO: Implement.
            throw new SAXParseException("Not implemented!", locator);
        }

        @Override
        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
            if (state == ParseState.IDLE) {
                if ("field".equals(qName)) {
                    state = ParseState.FIELD;
                    parseField(new ConfigurationDictionary(attributes));
                }
            } else if (state == ParseState.FIELD && "connections".equals(qName)) {
                state = ParseState.CONNECTIONS;
            } else if (state == ParseState.CONNECTIONS && "connection".equals(qName)) {
                parseConnection(new ConfigurationDictionary(attributes));
            } else if (state == ParseState.FIELD && "outputs".equals(qName)) {
                state = ParseState.OUTPUTS;
            } else if (state == ParseState.OUTPUTS && "output".equals(qName)) {
                parseOutput(new ConfigurationDictionary(attributes));
            } else if (state == ParseState.FIELD && "inputs".equals(qName)) {
                state = ParseState.INPUTS;
            } else if (state == ParseState.INPUTS && "input".equals(qName)) {
                parseInput(new ConfigurationDictionary(attributes));
            } else if (state == ParseState.FIELD && "triggers".equals(qName)) {
                state = ParseState.TRIGGERS;
            } else if (state == ParseState.TRIGGERS && "trigger".equals(qName)) {
                parseTrigger(new ConfigurationDictionary(attributes));
            } else {
                throw new SAXParseException("Invalid tag \"" + qName + "\"", locator);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
            throws SAXException {
            switch (state) {
                case IDLE:
                    break;

                case FIELD:
                    if ("field".equals(qName)) state = ParseState.IDLE;
                    break;

                case CONNECTIONS:
                    if ("connections".equals(qName)) state = ParseState.FIELD;
                    break;

                case INPUTS:
                    if ("inputs".equals(qName)) state = ParseState.FIELD;
                    break;

                case OUTPUTS:
                    if ("outputs".equals(qName)) state = ParseState.FIELD;
                    break;

                case TRIGGERS:
                    if ("triggers".equals(qName)) state = ParseState.TRIGGERS;
                    break;

                default:
                    break;

            }
        }
    };

    // Possible XML file sources are either from the file system, the jar or from a HTTP url.
    private enum XmlSource {
        FILE, RESOURCE, URL
    }

    @Override
    protected void loadImplementation(ConfigurationDictionary configuration) throws Exception {
        XmlSource source = configuration.get("source", XmlSource.FILE);

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        InputStream inputStream = null;

        switch (source) {
            case FILE:
                // Construct the filename using the mandatory parameter "file".
                File file = new File(configuration.get("file", String.class));
                if (!file.exists()) {
                    throw new ConfigurationException("File \"" + file.getAbsolutePath() + "\" not found!");
                }
                inputStream = new FileInputStream(file);
                break;

            case RESOURCE:
                String resource = configuration.get("resource", String.class);

                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
                if (inputStream == null) {
                    throw new ConfigurationException("Resource \"" + resource + "\" does not exist!");
                }
                break;

            case URL:
                URL url = new URL(configuration.get("url", String.class));
                inputStream = url.openStream();
                break;

            default:
                throw new ConfigurationException("\"" + source + "\" is not a valid configuration source!");
        }

        // Parse the XML file.
        saxParser.parse(inputStream, handler);
    }

    @Override
    protected void getUsage(StringBuilder builder) {
        builder
            .append("--file=<xml file>\n")
            .append("  Loads the jSCADA system definition from the given XML file.\n")
            .append("    <xml file>: Absolute or relative (to working directory) path to the XML file\n")
            .append("                containing the jSCADA definition.");
    }
}
