package ch.hevs.jscada.model;

import org.junit.Test;

import static org.junit.Assert.*;

public final class BooleanDataPointTest implements DataPointListener {
    private Process process = new Process();
    private BooleanDataPoint p;
    private Boolean reportedValue = null;

    @Override
    public void dataPointUpdated(DataPoint dataPoint) {
        reportedValue = ((BooleanDataPoint) dataPoint).getValue();
    }

    @Test
    public void createReadAndWrite() throws DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        assertEquals(p.getType(), DataPointType.BOOLEAN);
        assertFalse(p.getValue());
        p.select(this);
        p.setValue(true, this);
        assertTrue(p.getValue());
    }

    @Test(expected = DuplicateIdException.class)
    public void duplicateIdException() throws DuplicateIdException {
        new BooleanDataPoint("b1", process);
        new BooleanDataPoint("b1", process);
    }

    @Test
    public void stringConversion() throws ConversionException, DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        assertEquals(p.getStringValue(), "false");
        p.select(this);
        p.setStringValue("TruE", this);
        assertEquals(p.getStringValue(), "true");
    }

    @Test(expected = ConversionException.class)
    public void invalidString() throws ConversionException, DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        p.select(this);
        p.setStringValue("miaou", this);
    }

    @Test(expected = ConversionException.class)
    public void nullString() throws ConversionException, DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        p.select(this);
        p.setStringValue(null, this);
    }

    @Test
    public void doubleConversion() throws ConversionException, DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        assertEquals(p.getDoubleValue(), 0., 0);
        p.select(this);
        p.setValue(true, this);
        assertEquals(p.getDoubleValue(), 1, 0);
        p.setDoubleValue(0., this);
        assertFalse(p.getValue());
        p.setDoubleValue(1., this);
        assertTrue(p.getValue());
    }

    @Test
    public void changeListenerTrue() throws DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        p.addListener(this, true);
        p.select(this);
        p.setValue(false, this);
        assertNull(reportedValue);
        p.setValue(true, this);
        assertEquals(reportedValue, true);
        p.removeListener(this);
        p.setValue(false, this);
        assertEquals(reportedValue, true);
    }

    @Test
    public void changeListenerFalse() throws DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        p.select(this);
        p.setValue(true, this);
        p.addListener(this, true);
        p.setValue(true, this);
        assertNull(reportedValue);
        p.setValue(false, this);
        assertEquals(reportedValue, false);
        p.removeListener(this);
        p.setValue(true, this);
        assertEquals(reportedValue, false);
    }

    @Test
    public void updateListenerTrue() throws DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        p.select(this);
        p.addListener(this, false);
        p.setValue(false, this);
        assertEquals(reportedValue, false);
        p.setValue(true, this);
        assertEquals(reportedValue, true);
        p.removeListener(this);
        p.setValue(false, this);
        assertEquals(reportedValue, true);
    }

    @Test
    public void updateListenerFalse() throws DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        p.select(this);
        p.setValue(true, this);
        p.addListener(this, false);
        p.setValue(true, this);
        assertEquals(reportedValue, true);
        p.setValue(false, this);
        assertEquals(reportedValue, false);
        p.removeListener(this);
        p.setValue(true, this);
        assertEquals(reportedValue, false);
    }

    @Test(expected = SelectException.class)
    public void withoutSelection() throws DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        p.setValue(true, this);
    }

    @Test(expected = SelectException.class)
    public void selectWithNull() throws DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        p.select(null);
    }

    @Test(expected = SelectException.class)
    public void alreadySelected() throws DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        Object o = new Object();
        p.select(o);
        p.select(this);
    }

    @Test(expected = SelectException.class)
    public void alreadySelectedWhenWriting() throws DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        Object o = new Object();
        p.select(o);
        p.setValue(true, this);
    }

    @Test(expected = SelectException.class)
    public void deselect() throws DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        p.select(this);
        p.deselect(this);
        p.setValue(true, this);
    }

    @Test
    public void selectTwice() throws DuplicateIdException, SelectException {
        p = new BooleanDataPoint("b1", process);
        p.select(this);
        p.select(this);
    }

    @Test
    public void compareTo() throws DuplicateIdException {
        BooleanDataPoint b1 = new BooleanDataPoint("b1", process);
        BooleanDataPoint b2 = new BooleanDataPoint("b2", process);
        assertTrue(b1.compareTo(b2) < 0);
        assertTrue(b1.compareTo(b1) == 0);
        assertTrue(b2.compareTo(b1) > 0);
        assertTrue(b1.compareTo(null) > 0);
    }
}
