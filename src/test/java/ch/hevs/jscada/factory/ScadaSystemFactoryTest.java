package ch.hevs.jscada.factory;

import ch.hevs.jscada.config.ConfigurationDictionary;
import ch.hevs.jscada.config.ConfigurationException;
import ch.hevs.jscada.model.DuplicateIdException;
import ch.hevs.jscada.io.field.FieldConnection;
import ch.hevs.jscada.io.field.dummy.DummyConnection;
import ch.hevs.jscada.model.DataPointType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ScadaSystemFactoryTest extends ScadaSystemFactory {
    private final ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    private PrintStream originalStdout = System.out;

    @Override
    protected void getUsage(StringBuilder builder) {
        builder.append("test");
    }

    @Override
    protected void loadImplementation(ConfigurationDictionary configuration) throws Exception {
        assertEquals(configuration.get("test", String.class), "test");

        setSynchronisationInterval(1000);

        createConnection(DummyConnection.class.getCanonicalName(), "test", configuration);
        assertNotNull(getConnection("test"));

        Exception ex = null;
        try {
            createConnection(DummyConnection.class.getCanonicalName(), "test", configuration);
        } catch (DuplicateIdException e) {
            ex = e;
        }
        assertNotNull(ex);

        ex = null;
        try {
            createConnection(String.class.getCanonicalName(), "stupid", configuration);
        } catch (InstantiationException e) {
            ex = e;
        }
        assertNotNull(ex);

        ex = null;
        try {
            createConnection("toto.tata.titi.tutu", "not existing", configuration);
        } catch (ClassNotFoundException e) {
            ex = e;
        }
        assertNotNull(ex);

        FieldConnection dummy = (FieldConnection) getConnection("test");
        assertNotNull(dummy);

        addInput(dummy, DataPointType.BOOLEAN, "boolean", new ConfigurationDictionary("--id=1"));
        addInput(dummy, DataPointType.INTEGER, "integer", new ConfigurationDictionary("--id=2"));
        addInput(dummy, DataPointType.FLOATING_POINT, "float", new ConfigurationDictionary("--id=3"));

        addOutput(dummy, DataPointType.BOOLEAN, "boolean", new ConfigurationDictionary("--id=4"));
        addOutput(dummy, DataPointType.INTEGER, "integer", new ConfigurationDictionary("--id=5"));
        addOutput(dummy, DataPointType.FLOATING_POINT, "float", new ConfigurationDictionary("--id=6"));

        /*try {
            createDataPoint(DataPointType.BOOLEAN, "boolean", false);
        } catch (DuplicateIdException e) {
            ex = e;
        }
        assertNotNull(ex);

        ex = null;
        try {
            createDataPoint(DataPointType.INTEGER, "boolean", true);
        } catch (DuplicateIdException e) {
            ex = e;
        }
        assertNotNull(ex);*/

        addInput(dummy, DataPointType.BOOLEAN, "booleanInput", new ConfigurationDictionary("--id=123"));
    }

    @Before
    public void before() {
        System.setOut(new PrintStream(stdout));
    }

    @After
    public void after() {
        System.setOut(originalStdout);
    }

    @Test
    public void testUsage() {
        ScadaSystemFactory.main("help");
        assertTrue(stdout.size() > 20);
    }

    @Test
    public void testDefaultUsage() {
        ScadaSystemFactory.main("help", ScadaSystemFactoryWithoutUsageHelper.class.getCanonicalName());
        assertEquals(stdout.toString(), "Usage:\n" +
            "java -jar main ch.hevs.jscada.factory.ScadaSystemFactoryWithoutUsageHelper \n" +
            "  ch.hevs.jscada.factory.ScadaSystemFactoryWithoutUsageHelper does not provide any help.\n" +
            "\n");
    }

    @Test
    public void testCustomUsage() {
        ScadaSystemFactory.main("help", ScadaSystemFactoryTest.class.getCanonicalName());
        assertEquals(stdout.toString(), "Usage:\n" +
            "java -jar main ch.hevs.jscada.factory.ScadaSystemFactoryTest test\n" +
            "\n");
    }

    @Test
    public void testCustomUsageWithInvalidIdentifier() {
        ScadaSystemFactory.main("help", "miaou");
        assertEquals(stdout.toString(), "Invalid factory identifier!\n");
    }

    @Test(expected = IOException.class)
    public void loadWithInvalidIdentifier() throws Exception {
        ScadaSystemFactory.load("miaou");
    }

    @Test(expected = ConfigurationException.class)
    public void loadWithoutIdentifier() throws Exception {
        ScadaSystemFactory.load();
    }

    @Test(expected = IOException.class)
    public void loadWithInvalidIdentifierAndConfig() throws Exception {
        ScadaSystemFactory.load("miaou", new ConfigurationDictionary());
    }

    /*@Test
    public void loadingApi() throws Exception {
        ConfigurationDictionary dict = new ConfigurationDictionary();
        dict.set("test", "test");
        ScadaSystem scadaSystem = ScadaSystemFactory.load(ScadaSystemFactoryTest.class.getCanonicalName(), dict);
        assertNotNull(scadaSystem);
        assertFalse(scadaSystem.isActive());
        assertEquals(scadaSystem.getSynchronizationInterval(), 1000);
        assertNotNull(scadaSystem.getConnections().getConnection("test"));

        scadaSystem.start();

        scadaSystem.setSynchronizationInterval(2000);
        assertEquals(scadaSystem.getSynchronizationInterval(), 2000);

        scadaSystem.setSynchronizationInterval(0);
        assertTrue(scadaSystem.isActive());
        assertEquals(scadaSystem.getSynchronizationInterval(), 0);

        assertNotNull(scadaSystem.getProcess().getDataPoint("boolean"));
        assertNotNull(scadaSystem.getProcess().getDataPoint("integer"));
        assertNotNull(scadaSystem.getProcess().getDataPoint("float"));

        scadaSystem.stop();
    }*/

    @Test
    public void loadingMain() {
        ScadaSystemFactory.main(ScadaSystemFactoryTest.class.getCanonicalName(), "--test=test");
    }

    @Test
    public void xmlFactory() {
        ScadaSystemFactory.main(XmlScadaSystemFactory.class.getCanonicalName(), "--test=test");
    }
}
