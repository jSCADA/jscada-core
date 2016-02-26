package ch.hevs.jscada.config;

import ch.hevs.jscada.exception.DuplicateIdException;
import ch.hevs.jscada.io.ConnectionGroup;
import ch.hevs.jscada.io.dummy.DummyConnection;
import ch.hevs.jscada.model.BooleanDataPoint;
import ch.hevs.jscada.model.Process;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScadaSystemTest {
    @Test
    public void creationAndDefaultProperties() {
        ScadaSystem scadaSystem = new ScadaSystem();
        assertNotNull(scadaSystem.getProcess());
        assertTrue(scadaSystem.getProcess() instanceof Process);
        assertEquals(scadaSystem.getProcess().getDataPoints().size(), 0);

        assertNotNull(scadaSystem.getConnections());
        assertTrue(scadaSystem.getConnections() instanceof ConnectionGroup);
        assertEquals(scadaSystem.getConnections().connectionCount(), 0);

        assertEquals(scadaSystem.getSynchronizationInterval(), ScadaSystem.DEFAULT_SYNCHRONIZE_INTERVAL);
    }

    @Test
    public void settingProperties() throws DuplicateIdException {
        ScadaSystem scadaSystem = new ScadaSystem();

        scadaSystem.setSynchronizationInterval(0);
        assertEquals(scadaSystem.getSynchronizationInterval(), 0);

        new BooleanDataPoint("test", scadaSystem.getProcess());
        assertEquals(scadaSystem.getProcess().getDataPoints().size(), 1);
        assertNotNull(scadaSystem.getProcess().getDataPoint("test"));
        assertNotNull(scadaSystem.getProcess().getDataPoint("test", BooleanDataPoint.class));

        scadaSystem.getConnections().addConnection("test", new DummyConnection());
        assertEquals(scadaSystem.getConnections().connectionCount(), 1);
        assertNotNull(scadaSystem.getConnections().getConnection("test"));
    }

    @Test
    public void startingAndStopping() throws DuplicateIdException, InterruptedException {
        ScadaSystem scadaSystem = new ScadaSystem();

        assertFalse(scadaSystem.isActive());
        scadaSystem.start();
        assertTrue(scadaSystem.isActive());
        Thread.sleep(2000);
        assertTrue(scadaSystem.isActive());
        scadaSystem.stop();
        assertFalse(scadaSystem.isActive());
    }

    @Test
    public void withoutSynchronization() throws DuplicateIdException, InterruptedException {
        ScadaSystem scadaSystem = new ScadaSystem();
        scadaSystem.setSynchronizationInterval(ScadaSystem.NO_SYNCHRONIZATION);

        assertFalse(scadaSystem.isActive());
        scadaSystem.start();
        assertTrue(scadaSystem.isActive());
        Thread.sleep(500);
        assertTrue(scadaSystem.isActive());
        scadaSystem.stop();
        assertFalse(scadaSystem.isActive());
    }

    @Test
    public void changeSynchronizationIntervalWhileRunning() throws DuplicateIdException, InterruptedException {
        ScadaSystem scadaSystem = new ScadaSystem();

        assertFalse(scadaSystem.isActive());
        scadaSystem.start();
        assertTrue(scadaSystem.isActive());
        Thread.sleep(500);
        assertEquals(scadaSystem.getSynchronizationInterval(), ScadaSystem.DEFAULT_SYNCHRONIZE_INTERVAL);
        scadaSystem.setSynchronizationInterval(500);
        assertEquals(scadaSystem.getSynchronizationInterval(), 500);
        Thread.sleep(500);
        assertTrue(scadaSystem.isActive());
        scadaSystem.stop();
        assertFalse(scadaSystem.isActive());
    }
}
