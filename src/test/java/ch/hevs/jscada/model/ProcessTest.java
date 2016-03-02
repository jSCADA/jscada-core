package ch.hevs.jscada.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProcessTest {
    private Process process = new Process();

    @Test
    public void addDataPoints() throws DuplicateIdException {
        DataPoint b1 = new BooleanDataPoint("b1", process);
        DataPoint i1 = new IntegerDataPoint("i1", process);
        DataPoint f1 = new FloatDataPoint("f1", process);

        assertEquals(3, process.getDataPoints().size());

        assertTrue(process.getDataPoint("b1") == b1);
        assertTrue(process.getDataPoint("i1") == i1);
        assertTrue(process.getDataPoint("f1") == f1);

        assertTrue(process.getDataPoint("b1", BooleanDataPoint.class) == b1);
        assertTrue(process.getDataPoint("b1", IntegerDataPoint.class) == null);
        assertTrue(process.getDataPoint("b1", FloatDataPoint.class) == null);

        assertTrue(process.getDataPoint("i1", BooleanDataPoint.class) == null);
        assertTrue(process.getDataPoint("i1", IntegerDataPoint.class) == i1);
        assertTrue(process.getDataPoint("i1", FloatDataPoint.class) == null);

        assertTrue(process.getDataPoint("f1", BooleanDataPoint.class) == null);
        assertTrue(process.getDataPoint("f1", IntegerDataPoint.class) == null);
        assertTrue(process.getDataPoint("f1", FloatDataPoint.class) == f1);
    }

    @Test(expected = DuplicateIdException.class)
    public void duplicateIdException() throws DuplicateIdException {
        new BooleanDataPoint("b1", process);
        new IntegerDataPoint("b1", process);
    }

    @Test
    public void processListener() throws DuplicateIdException {
        final boolean[] added = {false};

        final ProcessListener listener = new ProcessListener() {
            @Override
            public void dataPointAdded(Process process, DataPoint dataPoint) {
                assertEquals(ProcessTest.this.process, process);
                assertEquals("test", dataPoint.getId());
                assertEquals(DataPointType.BOOLEAN, dataPoint.getType());
                added[0] = true;
            }
        };

        process.addProcessListener(listener);
        new BooleanDataPoint("test", process);

        assertTrue(added[0]);
        added[0] = false;

        process.removeProcessListener(listener);
        new IntegerDataPoint("test2", process);
        assertFalse(added[0]);
    }

    @Test
    public void removeListenerFromAllDataPoints() throws DuplicateIdException, SelectException {
        BooleanDataPoint b1 = new BooleanDataPoint("b1", process);

        final DataPointListener<BooleanDataPoint> listener = new DataPointListener<BooleanDataPoint>() {
            @Override
            public void dataPointUpdated(BooleanDataPoint dataPoint) {
                assertFalse(true);
            }
        };

        b1.addListener(listener, false);

        process.removeListenerFromAllDataPoints(listener);

        b1.select(this);
        b1.setValue(true, this);
    }
}
