package ch.hevs.jscada.model;

import org.junit.Test;

import static org.junit.Assert.*;

public final class FloatDataPointTest implements DataPointListener<FloatDataPoint> {
    private Process process = new Process();
    private FloatDataPoint p;
    private Double reportedValue = null;

    @Override
    public void dataPointUpdated(FloatDataPoint dataPoint) {
        reportedValue = dataPoint.getValue();
    }

    @Test
    public void createReadAndWrite() throws DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        assertEquals(p.getType(), DataPointType.FLOATING_POINT);
        assertEquals(p.getValue(), 0., 0.);
        p.select(this);
        p.setValue(42.42, this);
        assertEquals(p.getValue(), 42.42, 0.);
    }

    @Test(expected = DuplicateIdException.class)
    public void duplicateIdException() throws DuplicateIdException {
        new FloatDataPoint("f1", process);
        new FloatDataPoint("f1", process);
    }

    @Test
    public void stringConversion() throws ConversionException, DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        assertEquals(p.getStringValue(), "0.0");
        p.select(this);
        p.setStringValue("12.55", this);
        assertEquals(p.getStringValue(), "12.55");
    }

    @Test(expected = ConversionException.class)
    public void invalidString() throws ConversionException, DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        p.select(this);
        p.setStringValue("miaou", this);
    }

    @Test(expected = ConversionException.class)
    public void nullString() throws ConversionException, DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        p.select(this);
        p.setStringValue(null, this);
    }

    @Test
    public void doubleConversion() throws ConversionException, DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        assertEquals(p.getDoubleValue(), 0., 0);
        p.select(this);
        p.setValue(42.42, this);
        assertEquals(p.getDoubleValue(), 42.42, 0);
        p.setDoubleValue(0., this);
        assertEquals(p.getValue(), 0, 0);
        p.setDoubleValue(1., this);
        assertEquals(p.getValue(), 1, 0);
    }

    @Test
    public void changeListener() throws DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        p.addListener(this, true);
        p.select(this);
        p.setValue(0., this);
        assertNull(reportedValue);
        p.setValue(42.42, this);
        assertEquals(reportedValue, 42.42, 0.);
        p.removeListener(this);
        p.setValue(333.33, this);
        assertEquals(reportedValue, 42.42, 0.);
    }

    @Test
    public void updateListener() throws DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        p.addListener(this, false);
        p.select(this);
        p.setValue(0., this);
        assertEquals(reportedValue, 0., 0.);
        p.setValue(42.42, this);
        assertEquals(reportedValue, 42.42, 0.);
        p.removeListener(this);
        p.setValue(333.33, this);
        assertEquals(reportedValue, 42.42, 0.);
    }

    @Test(expected = SelectException.class)
    public void withoutSelection() throws DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        assertFalse(p.isSelected());
        p.setValue(42.42, this);
    }

    @Test(expected = SelectException.class)
    public void selectWithNull() throws DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        assertFalse(p.isSelected());
        p.select(null);
    }

    @Test(expected = SelectException.class)
    public void alreadySelected() throws DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        assertFalse(p.isSelected());
        Object o = new Object();
        p.select(o);
        assertTrue(p.isSelected());
        p.select(this);
    }

    @Test(expected = SelectException.class)
    public void alreadySelectedWhenWriting() throws DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        assertFalse(p.isSelected());
        Object o = new Object();
        p.select(o);
        assertTrue(p.isSelected());
        p.setValue(42.42, this);
    }

    @Test(expected = SelectException.class)
    public void deselect() throws DuplicateIdException, SelectException {
        p = new FloatDataPoint("f1", process);
        assertFalse(p.isSelected());
        p.select(this);
        assertTrue(p.isSelected());
        p.deselect(this);
        assertFalse(p.isSelected());
        p.setValue(42.42, this);
    }

    @Test
    public void selectTwice() throws DuplicateIdException, SelectException {
        p = new FloatDataPoint("b1", process);
        assertFalse(p.isSelected());
        p.select(this);
        assertTrue(p.isSelected());
        p.select(this);
        assertTrue(p.isSelected());
    }

    @Test
    public void compareTo() throws DuplicateIdException {
        FloatDataPoint f1 = new FloatDataPoint("f1", process);
        FloatDataPoint f2 = new FloatDataPoint("f2", process);
        assertTrue(f1.compareTo(f2) < 0);
        assertTrue(f1.compareTo(f1) == 0);
        assertTrue(f2.compareTo(f1) > 0);
        assertTrue(f1.compareTo(null) > 0);
    }
}
