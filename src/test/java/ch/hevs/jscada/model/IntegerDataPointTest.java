package ch.hevs.jscada.model;

import org.junit.Test;

import static org.junit.Assert.*;

public final class IntegerDataPointTest implements DataPointListener<IntegerDataPoint> {
    private Process process = new Process();
    private IntegerDataPoint p;
    private Long reportedValue;

    @Override
    public void dataPointUpdated(IntegerDataPoint dataPoint) {
        reportedValue = dataPoint.getValue();
    }

    @Test
    public void createReadAndWrite() throws DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        assertEquals(p.getType(), DataPointType.INTEGER);
        assertEquals(p.getValue(), 0);
        p.select(this);
        p.setValue(42, this);
        assertEquals(p.getValue(), 42);
    }

    @Test(expected = DuplicateIdException.class)
    public void duplicateIdException() throws DuplicateIdException {
        new IntegerDataPoint("i1", process);
        new IntegerDataPoint("i1", process);
    }

    @Test
    public void stringConversion() throws ConversionException, DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        assertEquals(p.getStringValue(), "0");
        p.select(this);
        p.setStringValue("123", this);
        assertEquals(p.getStringValue(), "123");
    }

    @Test(expected = ConversionException.class)
    public void invalidString() throws ConversionException, DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        p.select(this);
        p.setStringValue("miaou", this);
    }

    @Test(expected = ConversionException.class)
    public void nullString() throws ConversionException, DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        p.select(this);
        p.setStringValue(null, this);
    }

    @Test
    public void doubleConversion() throws ConversionException, DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        assertEquals(p.getDoubleValue(), 0., 0);
        p.select(this);
        p.setValue(42, this);
        assertEquals(p.getDoubleValue(), 42, 0);
        p.setDoubleValue(0., this);
        assertEquals(p.getValue(), 0);
        p.setDoubleValue(1., this);
        assertEquals(p.getValue(), 1);
    }

    @Test
    public void changeListener() throws DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        p.addListener(this, true);
        p.select(this);
        p.setValue(0, this);
        assertNull(reportedValue);
        p.setValue(42, this);
        assertEquals(reportedValue.longValue(), 42L);
        p.removeListener(this);
        p.setValue(333, this);
        assertEquals(reportedValue.longValue(), 42L);
    }

    @Test
    public void updateListener() throws DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        p.addListener(this, false);
        p.select(this);
        p.setValue(0, this);
        assertEquals(reportedValue.longValue(), 0L);
        p.setValue(42, this);
        assertEquals(reportedValue.longValue(), 42L);
        p.removeListener(this);
        p.setValue(333, this);
        assertEquals(reportedValue.longValue(), 42L);
    }

    @Test(expected = SelectException.class)
    public void withoutSelection() throws DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        assertFalse(p.isSelected());
        p.setValue(42, this);
    }

    @Test(expected = SelectException.class)
    public void selectWithNull() throws DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        assertFalse(p.isSelected());
        p.select(null);
    }

    @Test(expected = SelectException.class)
    public void alreadySelected() throws DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        assertFalse(p.isSelected());
        Object o = new Object();
        p.select(o);
        assertTrue(p.isSelected());
        p.select(this);
    }

    @Test(expected = SelectException.class)
    public void alreadySelectedWhenWriting() throws DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        assertFalse(p.isSelected());
        Object o = new Object();
        p.select(o);
        assertTrue(p.isSelected());
        p.setValue(42, this);
    }

    @Test(expected = SelectException.class)
    public void deselect() throws DuplicateIdException, SelectException {
        p = new IntegerDataPoint("i1", process);
        assertFalse(p.isSelected());
        p.select(this);
        assertTrue(p.isSelected());
        p.deselect(this);
        assertFalse(p.isSelected());
        p.setValue(42, this);
    }

    @Test
    public void selectTwice() throws DuplicateIdException, SelectException {
        p = new IntegerDataPoint("b1", process);
        assertFalse(p.isSelected());
        p.select(this);
        assertTrue(p.isSelected());
        p.select(this);
        assertTrue(p.isSelected());
    }

    @Test
    public void compareTo() throws DuplicateIdException {
        IntegerDataPoint i1 = new IntegerDataPoint("i1", process);
        IntegerDataPoint i2 = new IntegerDataPoint("i2", process);
        assertTrue(i1.compareTo(i2) < 0);
        assertTrue(i1.compareTo(i1) == 0);
        assertTrue(i2.compareTo(i1) > 0);
        assertTrue(i1.compareTo(null) > 0);
    }
}
