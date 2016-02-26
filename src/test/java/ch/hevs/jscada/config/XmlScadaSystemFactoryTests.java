package ch.hevs.jscada.config;

import ch.hevs.jscada.exception.ConfigurationException;
import ch.hevs.jscada.io.dummy.DummyConnection;
import ch.hevs.jscada.model.*;
import ch.hevs.jscada.model.Process;
import fi.iki.elonen.SimpleWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXParseException;

import java.io.*;

import static org.junit.Assert.*;

public class XmlScadaSystemFactoryTests {
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();
    private File simpleConfigurationFile;
    private SimpleWebServer webServer;

    @Before
    public void before() throws IOException {
        simpleConfigurationFile = folder.newFile();
        OutputStream out = new FileOutputStream(simpleConfigurationFile);
        InputStream in =
            Thread.currentThread().getContextClassLoader().getResourceAsStream("configurations/xml/simple.xml");
        byte[] buffer = new byte[64];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.close();

        webServer = new SimpleWebServer("0.0.0.0", 8080, folder.getRoot(), true);
        webServer.start();
    }

    @After
    public void after() {
        webServer.stop();
    }

    @Test
    public void simpleTestFromResources() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/simple.xml");
        ScadaSystem scadaSystem = ScadaSystemFactory.load("xml", config);

        assertNotNull(scadaSystem);

        assertEquals(scadaSystem.getSynchronizationInterval(), 1000);

        assertNotNull(scadaSystem.getConnections().getConnection("DUMMY"));
        assertTrue(scadaSystem.getConnections().getConnection("DUMMY") instanceof DummyConnection);

        Process process = scadaSystem.getProcess();
        assertNotNull(process);

        DataPoint datapoint = process.getDataPoint("aBooleanInput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof BooleanDataPoint);

        datapoint = process.getDataPoint("anIntegerInput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof IntegerDataPoint);

        datapoint = process.getDataPoint("aFloatInput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof FloatDataPoint);

        datapoint = process.getDataPoint("aBooleanOutput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof BooleanDataPoint);

        datapoint = process.getDataPoint("anIntegerOutput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof IntegerDataPoint);

        datapoint = process.getDataPoint("aFloatOutput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof FloatDataPoint);
    }

    @Test
    public void simpleTestFromFile() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("file", simpleConfigurationFile.getAbsolutePath());
        ScadaSystem scadaSystem = ScadaSystemFactory.load("xml", config);

        assertNotNull(scadaSystem);

        assertEquals(scadaSystem.getSynchronizationInterval(), 1000);

        assertNotNull(scadaSystem.getConnections().getConnection("DUMMY"));
        assertTrue(scadaSystem.getConnections().getConnection("DUMMY") instanceof DummyConnection);

        Process process = scadaSystem.getProcess();
        assertNotNull(process);

        DataPoint datapoint = process.getDataPoint("aBooleanInput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof BooleanDataPoint);

        datapoint = process.getDataPoint("anIntegerInput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof IntegerDataPoint);

        datapoint = process.getDataPoint("aFloatInput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof FloatDataPoint);

        datapoint = process.getDataPoint("aBooleanOutput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof BooleanDataPoint);

        datapoint = process.getDataPoint("anIntegerOutput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof IntegerDataPoint);

        datapoint = process.getDataPoint("aFloatOutput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof FloatDataPoint);
    }

    @Test
    public void simpleTestFromUrl() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "URL");
        config.set("url", "http://localhost:8080/" + simpleConfigurationFile.getName());
        ScadaSystem scadaSystem = ScadaSystemFactory.load("xml", config);

        assertNotNull(scadaSystem);

        assertEquals(scadaSystem.getSynchronizationInterval(), 1000);

        assertNotNull(scadaSystem.getConnections().getConnection("DUMMY"));
        assertTrue(scadaSystem.getConnections().getConnection("DUMMY") instanceof DummyConnection);

        Process process = scadaSystem.getProcess();
        assertNotNull(process);

        DataPoint datapoint = process.getDataPoint("aBooleanInput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof BooleanDataPoint);

        datapoint = process.getDataPoint("anIntegerInput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof IntegerDataPoint);

        datapoint = process.getDataPoint("aFloatInput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof FloatDataPoint);

        datapoint = process.getDataPoint("aBooleanOutput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof BooleanDataPoint);

        datapoint = process.getDataPoint("anIntegerOutput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof IntegerDataPoint);

        datapoint = process.getDataPoint("aFloatOutput");
        assertNotNull(datapoint);
        assertTrue(datapoint instanceof FloatDataPoint);

        scadaSystem.start();
        Thread.sleep(3000);
        assertTrue(scadaSystem.isActive());
        scadaSystem.stop();
        assertFalse(scadaSystem.isActive());
    }

    @Test(expected = ConfigurationException.class)
    public void missingFileParameter() throws Exception {
        ScadaSystemFactory.load("xml", new ConfigurationDictionary());
    }

    @Test(expected = ConfigurationException.class)
    public void missingResourceParameter() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = ConfigurationException.class)
    public void invalidFileParameter() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("file", "thisfiledoesnotexistandwillprobablynever.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = ConfigurationException.class)
    public void invalidResourceParameter() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "thisresourcedoesnotexistandwillprobablynever.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void invalidFieldSynchronizationInterval() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/invalid-field-synchronizeInterval.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void missingConnectionClass() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/missing-connection-class.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void missingConnectionId() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/missing-connection-id.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void inexistentInputConnectionRef() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/inexistent-input-connection-ref.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void inexistentOutputConnectionRef() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/inexistent-output-connection-ref.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void missingInputPointRef() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/missing-input-pointRef.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void missingOutputPointRef() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/missing-output-pointRef.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test
    public void completeInputRef() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/complete-input-ref.xml");
        ScadaSystem scadaSystem = ScadaSystemFactory.load("xml", config);

        DataPoint dataPoint = scadaSystem.getProcess().getDataPoint("G.N.P");
        assertNotNull(dataPoint);
        assertTrue(dataPoint instanceof BooleanDataPoint);
    }

    @Test
    public void completeOutputRef() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/complete-output-ref.xml");
        ScadaSystem scadaSystem = ScadaSystemFactory.load("xml", config);

        DataPoint dataPoint = scadaSystem.getProcess().getDataPoint("G.N.P");
        assertNotNull(dataPoint);
        assertTrue(dataPoint instanceof IntegerDataPoint);
    }

    @Test(expected = SAXParseException.class)
    public void invalidTagInField() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/invalid-tag-in-field.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void invalidTagInConnections() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/invalid-tag-in-connections.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void invalidTagInConnection() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/invalid-tag-in-connection.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void invalidTagInCInputs() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/invalid-tag-in-inputs.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void invalidTagInCInput() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/invalid-tag-in-input.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void invalidTagInOutputs() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/invalid-tag-in-outputs.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test(expected = SAXParseException.class)
    public void invalidTagInOutput() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("source", "RESOURCE");
        config.set("resource", "configurations/xml/invalid-tag-in-output.xml");
        ScadaSystemFactory.load("xml", config);
    }

    @Test
    public void usage() throws Exception {
        ScadaSystemFactory.main("help", "xml");
    }

    @Test
    public void testSyncIntervalChangeDuringActive() throws Exception {
        ConfigurationDictionary config = new ConfigurationDictionary();
        config.set("file", simpleConfigurationFile.getAbsolutePath());
        ScadaSystem scadaSystem = ScadaSystemFactory.load("xml", config);

        assertFalse(scadaSystem.isActive());
        scadaSystem.start();
        Thread.sleep(2);
        assertTrue(scadaSystem.isActive());
        scadaSystem.setSynchronizationInterval(500);
        assertEquals(scadaSystem.getSynchronizationInterval(), 500);
        assertTrue(scadaSystem.isActive());
        Thread.sleep(2);
        scadaSystem.stop();
        assertFalse(scadaSystem.isActive());
    }
}
