import ch.hevs.jscada.config.ConfigurationDictionary;
import ch.hevs.jscada.exception.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigurationDictionaryTest {
    private ConfigurationDictionary dict = new ConfigurationDictionary();

    @Before
    public void before() {
        dict.set("aBoolean", true);
        dict.set("anotherBoolean", Boolean.FALSE);
        dict.set("sBoolean1", "true");
        dict.set("sBoolean2", "TRUE");
        dict.set("sBoolean3", "True");
        dict.set("sBoolean4", "false");
        dict.set("sBoolean5", "FALSE");
        dict.set("sBoolean6", "False");

        dict.set("byte", (byte)8);
        dict.set("byteo", new Byte((byte)9));
        dict.set("short", (short)16);
        dict.set("shorto", new Short((short)17));
        dict.set("int", (int)32);
        dict.set("into", new Integer(33));
        dict.set("long", 64L);
        dict.set("longo", new Long(65));

        dict.set("float", 12.34f);
        dict.set("floato", new Float(56.78f));
        dict.set("double", 43.21);
        dict.set("doubleo", new Double(87.65));

        dict.set("string", "VOID");
        dict.set("stringInt", "64");
        dict.set("stringFloat", "1234.5678");
    }

    @Test
    public void getBoolean() throws ConfigurationException {
        boolean b = dict.get("aBoolean", Boolean.class);
        assertEquals(b, true);
        b = dict.get("anotherBoolean", Boolean.class);
        assertEquals(b, false);
        b = dict.get("sBoolean1", Boolean.class);
        assertEquals(b, true);
        b = dict.get("sBoolean2", Boolean.class);
        assertEquals(b, true);
        b = dict.get("sBoolean3", Boolean.class);
        assertEquals(b, true);
        b = dict.get("sBoolean4", Boolean.class);
        assertEquals(b, false);
        b = dict.get("sBoolean5", Boolean.class);
        assertEquals(b, false);
        b = dict.get("sBoolean6", Boolean.class);
        assertEquals(b, false);
    }

    @Test
    public void getBooleanObjects() throws ConfigurationException {
        Boolean b = dict.get("aBoolean", Boolean.class);
        assertEquals(b, true);
        b = dict.get("anotherBoolean", Boolean.class);
        assertEquals(b, false);
        b = dict.get("sBoolean1", Boolean.class);
        assertEquals(b, true);
        b = dict.get("sBoolean2", Boolean.class);
        assertEquals(b, true);
        b = dict.get("sBoolean3", Boolean.class);
        assertEquals(b, true);
        b = dict.get("sBoolean4", Boolean.class);
        assertEquals(b, false);
        b = dict.get("sBoolean5", Boolean.class);
        assertEquals(b, false);
        b = dict.get("sBoolean6", Boolean.class);
        assertEquals(b, false);
    }

    @Test(expected = ConfigurationException.class)
    public void getBooleanFromByte() throws ConfigurationException {
        dict.get("byte", Boolean.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getBooleanFromShort() throws ConfigurationException {
        dict.get("short", Boolean.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getBooleanFromInt() throws ConfigurationException {
        dict.get("int", Boolean.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getBooleanFromLong() throws ConfigurationException {
        dict.get("long", Boolean.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getBooleanFromFloat() throws ConfigurationException {
        dict.get("float", Boolean.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getBooleanFromDouble() throws ConfigurationException {
        dict.get("double", Boolean.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getBooleanFromStringInt() throws ConfigurationException {
        dict.get("stringInt", Boolean.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getBooleanFromStringFloat() throws ConfigurationException {
        dict.get("stringFloat", Boolean.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getBooleanFromString() throws ConfigurationException {
        dict.get("string", Boolean.class);
    }

    @Test
    public void getByte() throws ConfigurationException {
        byte b = dict.get("byte", Byte.class);
        assertEquals(b, 8);
        b = dict.get("byteo", Byte.class);
        assertEquals(b, 9);
        b = dict.get("stringInt", Byte.class);
        assertEquals(b, 64);
    }

    @Test
    public void getByteObject() throws ConfigurationException {
        Byte b = dict.get("byte", Byte.class);
        assertEquals(b, Byte.valueOf((byte) 8));
        b = dict.get("byteo", Byte.class);
        assertEquals(b, Byte.valueOf((byte) 9));
        b = dict.get("stringInt", Byte.class);
        assertEquals(b, Byte.valueOf((byte) 64));
    }

    @Test(expected = ConfigurationException.class)
    public void getByteFromShort() throws ConfigurationException {
        dict.get("short", Byte.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getByteFromInt() throws ConfigurationException {
        dict.get("int", Byte.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getByteFromLong() throws ConfigurationException {
        dict.get("long", Byte.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getByteFromFloat() throws ConfigurationException {
        dict.get("float", Byte.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getByteFromDouble() throws ConfigurationException {
        dict.get("double", Byte.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getByteFromStringFloat() throws ConfigurationException {
        dict.get("stringFloat", Byte.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getByteFromString() throws ConfigurationException {
        dict.get("string", Byte.class);
    }

    @Test
    public void getShort() throws ConfigurationException {
        short s = dict.get("byte", Short.class);
        assertEquals(s, 8);
        s = dict.get("byteo", Short.class);
        assertEquals(s, 9);
        s = dict.get("short", Short.class);
        assertEquals(s, 16);
        s = dict.get("shorto", Short.class);
        assertEquals(s, 17);
        s = dict.get("stringInt", Short.class);
        assertEquals(s, 64);
    }

    @Test
    public void getShortObject() throws ConfigurationException {
        Short s = dict.get("byte", Short.class);
        assertEquals(s, Short.valueOf((short) 8));
        s = dict.get("byteo", Short.class);
        assertEquals(s, Short.valueOf((short) 9));
        s = dict.get("short", Short.class);
        assertEquals(s, Short.valueOf((short) 16));
        s = dict.get("shorto", Short.class);
        assertEquals(s, Short.valueOf((short) 17));
        s = dict.get("stringInt", Short.class);
        assertEquals(s, Short.valueOf((short) 64));
    }

    @Test(expected = ConfigurationException.class)
    public void getShortFromInt() throws ConfigurationException {
        dict.get("int", Short.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getShortFromLong() throws ConfigurationException {
        dict.get("long", Short.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getShortFromFloat() throws ConfigurationException {
        dict.get("float", Short.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getShortFromDouble() throws ConfigurationException {
        dict.get("double", Short.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getShortFromStringFloat() throws ConfigurationException {
        dict.get("stringFloat", Short.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getShortFromString() throws ConfigurationException {
        dict.get("string", Short.class);
    }

    @Test
    public void getInt() throws ConfigurationException {
        int i = dict.get("byte", Integer.class);
        assertEquals(i, 8);
        i = dict.get("byteo", Integer.class);
        assertEquals(i, 9);
        i = dict.get("short", Integer.class);
        assertEquals(i, 16);
        i = dict.get("shorto", Integer.class);
        assertEquals(i, 17);
        i = dict.get("int", Integer.class);
        assertEquals(i, 32);
        i = dict.get("into", Integer.class);
        assertEquals(i, 33);
        i = dict.get("stringInt", Integer.class);
        assertEquals(i, 64);
    }

    @Test
    public void getIntObject() throws ConfigurationException {
        Integer i = dict.get("byte", Integer.class);
        assertEquals(i, Integer.valueOf(8));
        i = dict.get("byteo", Integer.class);
        assertEquals(i, Integer.valueOf(9));
        i = dict.get("short", Integer.class);
        assertEquals(i, Integer.valueOf(16));
        i = dict.get("shorto", Integer.class);
        assertEquals(i, Integer.valueOf(17));
        i = dict.get("int", Integer.class);
        assertEquals(i, Integer.valueOf(32));
        i = dict.get("into", Integer.class);
        assertEquals(i, Integer.valueOf(33));
        i = dict.get("stringInt", Integer.class);
        assertEquals(i, Integer.valueOf(64));
    }

    @Test(expected = ConfigurationException.class)
    public void getIntFromLong() throws ConfigurationException {
        dict.get("long", Integer.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getIntFromFloat() throws ConfigurationException {
        dict.get("float", Integer.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getIntFromDouble() throws ConfigurationException {
        dict.get("double", Integer.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getIntFromStringFloat() throws ConfigurationException {
        dict.get("stringFloat", Integer.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getIntFromString() throws ConfigurationException {
        dict.get("string", Integer.class);
    }

    @Test
    public void getLong() throws ConfigurationException {
        long l = dict.get("byte", Long.class);
        assertEquals(l, 8);
        l = dict.get("byteo", Long.class);
        assertEquals(l, 9);
        l = dict.get("short", Long.class);
        assertEquals(l, 16);
        l = dict.get("shorto", Long.class);
        assertEquals(l, 17);
        l = dict.get("int", Long.class);
        assertEquals(l, 32);
        l = dict.get("into", Long.class);
        assertEquals(l, 33);
        l = dict.get("long", Long.class);
        assertEquals(l, 64);
        l = dict.get("longo", Long.class);
        assertEquals(l, 65);
        l = dict.get("stringInt", Long.class);
        assertEquals(l, 64);
    }

    @Test
    public void getLongObject() throws ConfigurationException {
        Long l = dict.get("byte", Long.class);
        assertEquals(l, Long.valueOf(8));
        l = dict.get("byteo", Long.class);
        assertEquals(l, Long.valueOf(9));
        l = dict.get("short", Long.class);
        assertEquals(l, Long.valueOf(16));
        l = dict.get("shorto", Long.class);
        assertEquals(l, Long.valueOf(17));
        l = dict.get("long", Long.class);
        assertEquals(l, Long.valueOf(64));
        l = dict.get("longo", Long.class);
        assertEquals(l, Long.valueOf(65));
        l = dict.get("stringInt", Long.class);
        assertEquals(l, Long.valueOf(64));
    }

    @Test(expected = ConfigurationException.class)
    public void getLongFromFloat() throws ConfigurationException {
        dict.get("float", Long.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getLongFromDouble() throws ConfigurationException {
        dict.get("double", Long.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getLongFromStringFloat() throws ConfigurationException {
        dict.get("stringFloat", Long.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getLongFromString() throws ConfigurationException {
        dict.get("string", Long.class);
    }

    @Test
    public void getFloat() throws ConfigurationException {
        float f = dict.get("byte", Float.class);
        assertEquals(f, 8f, 0f);
        f = dict.get("byteo", Float.class);
        assertEquals(f, 9f, 0);
        f = dict.get("short", Float.class);
        assertEquals(f, 16f, 0);
        f = dict.get("shorto", Float.class);
        assertEquals(f, 17f, 0);
        f = dict.get("int", Float.class);
        assertEquals(f, 32f, 0);
        f = dict.get("into", Float.class);
        assertEquals(f, 33f, 0);
        f = dict.get("long", Float.class);
        assertEquals(f, 64f, 0);
        f = dict.get("longo", Float.class);
        assertEquals(f, 65f, 0);
        f = dict.get("float", Float.class);
        assertEquals(f, 12.34f, 0);
        f = dict.get("floato", Float.class);
        assertEquals(f, 56.78f, 0);
        f = dict.get("stringInt", Float.class);
        assertEquals(f, 64f, 0);
        f = dict.get("stringFloat", Float.class);
        assertEquals(f, 1234.5678, 0.01);
    }

    @Test
    public void getFloatObject() throws ConfigurationException {
        Float f = dict.get("byte", Float.class);
        assertEquals(f, Float.valueOf(8));
        f = dict.get("byteo", Float.class);
        assertEquals(f, Float.valueOf(9));
        f = dict.get("short", Float.class);
        assertEquals(f, Float.valueOf(16));
        f = dict.get("shorto", Float.class);
        assertEquals(f, Float.valueOf(17));
        f = dict.get("long", Float.class);
        assertEquals(f, Float.valueOf(64));
        f = dict.get("longo", Float.class);
        assertEquals(f, Float.valueOf(65));
        f = dict.get("float", Float.class);
        assertEquals(f, Float.valueOf(12.34f));
        f = dict.get("floato", Float.class);
        assertEquals(f, Float.valueOf(56.78f));
        f = dict.get("stringInt", Float.class);
        assertEquals(f, Float.valueOf(64));
        f = dict.get("stringFloat", Float.class);
        assertEquals(f, Float.valueOf(1234.5678f));
    }

    @Test(expected = ConfigurationException.class)
    public void getFloatFromDouble() throws ConfigurationException {
        dict.get("double", Float.class);
    }

    @Test(expected = ConfigurationException.class)
    public void getFloatFromString() throws ConfigurationException {
        dict.get("string", Float.class);
    }

    @Test
    public void getDouble() throws ConfigurationException {
        double d = dict.get("byte", Double.class);
        assertEquals(d, 8, 0);
        d = dict.get("byteo", Double.class);
        assertEquals(d, 9, 0);
        d = dict.get("short", Double.class);
        assertEquals(d, 16, 0);
        d = dict.get("shorto", Double.class);
        assertEquals(d, 17, 0);
        d = dict.get("int", Double.class);
        assertEquals(d, 32, 0);
        d = dict.get("into", Double.class);
        assertEquals(d, 33, 0);
        d = dict.get("long", Double.class);
        assertEquals(d, 64, 0);
        d = dict.get("longo", Double.class);
        assertEquals(d, 65, 0);
        d = dict.get("float", Double.class);
        assertEquals(d, 12.34, 0.01);
        d = dict.get("floato", Double.class);
        assertEquals(d, 56.78, 0.01);
        d = dict.get("double", Double.class);
        assertEquals(d, 43.21, 0.01);
        d = dict.get("doubleo", Double.class);
        assertEquals(d, 87.65, 0.01);
        d = dict.get("stringInt", Double.class);
        assertEquals(d, 64, 0.01);
        d = dict.get("stringFloat", Double.class);
        assertEquals(d, 1234.5678, 0.01);
    }

    @Test
    public void getDoubleObject() throws ConfigurationException {
        Double d = dict.get("byte", Double.class);
        assertEquals(d, Double.valueOf(8));
        d = dict.get("byteo", Double.class);
        assertEquals(d, Double.valueOf(9));
        d = dict.get("short", Double.class);
        assertEquals(d, Double.valueOf(16));
        d = dict.get("shorto", Double.class);
        assertEquals(d, Double.valueOf(17));
        d = dict.get("long", Double.class);
        assertEquals(d, Double.valueOf(64));
        d = dict.get("longo", Double.class);
        assertEquals(d, Double.valueOf(65));
        d = dict.get("float", Double.class);
        assertEquals(d, Double.valueOf(12.34f));
        d = dict.get("floato", Double.class);
        assertEquals(d, Double.valueOf(56.78f));
        d = dict.get("double", Double.class);
        assertEquals(d, Double.valueOf(43.21f), 0.01);
        d = dict.get("doubleo", Double.class);
        assertEquals(d, Double.valueOf(87.65f), 0.01);
        d = dict.get("stringInt", Double.class);
        assertEquals(d, Double.valueOf(64), 0.01);
        d = dict.get("stringFloat", Double.class);
        assertEquals(d, Double.valueOf(1234.5678f), 0.01);
    }

    @Test(expected = ConfigurationException.class)
    public void getDoubleFromString() throws ConfigurationException {
        dict.get("string", Double.class);
    }

    @Test
    public void getString() throws ConfigurationException {
        String s = dict.get("byte", String.class);
        assertEquals(s, "8");
        s = dict.get("byteo", String.class);
        assertEquals(s, "9");
        s = dict.get("short", String.class);
        assertEquals(s, "16");
        s = dict.get("shorto", String.class);
        assertEquals(s, "17");
        s = dict.get("int", String.class);
        assertEquals(s, "32");
        s = dict.get("into", String.class);
        assertEquals(s, "33");
        s = dict.get("long", String.class);
        assertEquals(s, "64");
        s = dict.get("longo", String.class);
        assertEquals(s, "65");
        s = dict.get("float", String.class);
        assertEquals(s, "12.34");
        s = dict.get("floato", String.class);
        assertEquals(s, "56.78");
        s = dict.get("double", String.class);
        assertEquals(s, "43.21");
        s = dict.get("doubleo", String.class);
        assertEquals(s, "87.65");
        s = dict.get("stringInt", String.class);
        assertEquals(s, "64");
        s = dict.get("stringFloat", String.class);
        assertEquals(s, "1234.5678");
        s = dict.get("string", String.class);
        assertEquals(s, "VOID");
    }

    @Test(expected = ConfigurationException.class)
    public void missingKey() throws ConfigurationException {
        dict.get("somemissingkeythingy", Boolean.class);
    }
}
