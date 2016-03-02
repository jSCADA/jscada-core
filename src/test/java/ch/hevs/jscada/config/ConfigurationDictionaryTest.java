package ch.hevs.jscada.config;

import ch.hevs.jscada.model.DataPoint;
import ch.hevs.jscada.model.DataPointType;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        dict.set("byte", (byte) 8);
        dict.set("byteo", new Byte((byte) 9));
        dict.set("short", (short) 16);
        dict.set("shorto", new Short((short) 17));
        dict.set("int", (int) 32);
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
        b = dict.get("somemissingkey", true);
        assertEquals(b, true);
        b = dict.get("somemissingkey", false);
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
        b = dict.get("somemissingkey", true);
        assertEquals(b, true);
        b = dict.get("somemissingkey", false);
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
        b = dict.get("somemissingkey", (byte) 55);
        assertEquals(b, 55);
    }

    @Test
    public void getByteObject() throws ConfigurationException {
        Byte b = dict.get("byte", Byte.class);
        assertEquals(b, Byte.valueOf((byte) 8));
        b = dict.get("byteo", Byte.class);
        assertEquals(b, Byte.valueOf((byte) 9));
        b = dict.get("stringInt", Byte.class);
        assertEquals(b, Byte.valueOf((byte) 64));
        b = dict.get("somemissingkey", (byte) 55);
        assertEquals(b, Byte.valueOf((byte) 55));
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
        s = dict.get("somemissingkey", (short) 55);
        assertEquals(s, (short) 55);
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
        s = dict.get("somemissingkey", (short) 55);
        assertEquals(s, Short.valueOf((short) 55));
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
        i = dict.get("somemissingkey", 55);
        assertEquals(i, 55);
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
        i = dict.get("somemissingkey", 55);
        assertEquals(i, Integer.valueOf(55));
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
        l = dict.get("somemissingkey", 55L);
        assertEquals(l, 55L);
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
        l = dict.get("somemissingkey", 55L);
        assertEquals(l, Long.valueOf(55L));
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
        f = dict.get("somemissingkey", 55.5f);
        assertEquals(f, 55.5f, 0);
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
        f = dict.get("somemissingkey", 55.5f);
        assertEquals(f, Float.valueOf(55.5f));
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
        d = dict.get("somemissingkey", 55.5);
        assertEquals(d, 55.5, 0.01);
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
        d = dict.get("somemissingkey", 55.5);
        assertEquals(d, Double.valueOf(55.5), 0.01);
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
        s = dict.get("somemissingkey", "NOTHING");
        assertEquals(s, "NOTHING");
    }

    @Test(expected = ConfigurationException.class)
    public void missingKey() throws ConfigurationException {
        dict.get("somemissingkey", Boolean.class);
    }

    @Test
    public void getByteInRange() throws ConfigurationException {
        byte b = dict.get("byte", ConfigurationDictionary.inRange((byte) 4, (byte) 12));
        assertEquals(b, (byte) 8);
    }

    @Test(expected = ConfigurationException.class)
    public void getByteInRangeToSmall() throws ConfigurationException {
        dict.get("byte", ConfigurationDictionary.inRange((byte) 15, (byte) 22));
    }

    @Test(expected = ConfigurationException.class)
    public void getByteInRangeToBig() throws ConfigurationException {
        dict.get("byte", ConfigurationDictionary.inRange((byte) 1, (byte) 4));
    }

    @Test
    public void getShortInRange() throws ConfigurationException {
        short s = dict.get("short", ConfigurationDictionary.inRange((short) 4, (short) 22));
        assertEquals(s, (short) 16);
    }

    @Test(expected = ConfigurationException.class)
    public void getShortInRangeToSmall() throws ConfigurationException {
        short s = dict.get("short", ConfigurationDictionary.inRange((byte) 17, (byte) 22));
    }

    @Test(expected = ConfigurationException.class)
    public void getShortInRangeToBig() throws ConfigurationException {
        short s = dict.get("short", ConfigurationDictionary.inRange((short) 1, (short) 12));
    }

    @Test
    public void getIntInRange() throws ConfigurationException {
        int i = dict.get("int", ConfigurationDictionary.inRange(4, 42));
        assertEquals(i, 32);
    }

    @Test(expected = ConfigurationException.class)
    public void getIntInRangeToSmall() throws ConfigurationException {
        int i = dict.get("int", ConfigurationDictionary.inRange(35, 200));
    }

    @Test(expected = ConfigurationException.class)
    public void getIntInRangeToBig() throws ConfigurationException {
        int i = dict.get("int", ConfigurationDictionary.inRange(1, 12));
    }

    @Test
    public void getLongInRange() throws ConfigurationException {
        long l = dict.get("long", ConfigurationDictionary.inRange(4L, 5000L));
        assertEquals(l, 64);
    }

    @Test(expected = ConfigurationException.class)
    public void getLongInRangeToSmall() throws ConfigurationException {
        long l = dict.get("long", ConfigurationDictionary.inRange(200L, 201L));
    }

    @Test(expected = ConfigurationException.class)
    public void getLongInRangeToBig() throws ConfigurationException {
        long l = dict.get("long", ConfigurationDictionary.inRange(1L, 1L));
    }

    @Test
    public void getFloatInRange() throws ConfigurationException {
        float f = dict.get("float", ConfigurationDictionary.inRange(-500f, 500f));
        assertEquals(f, 12.34, 0.01);
    }

    @Test(expected = ConfigurationException.class)
    public void getFloatInRangeToSmall() throws ConfigurationException {
        float f = dict.get("float", ConfigurationDictionary.inRange(2000f, 2005f));
    }

    @Test(expected = ConfigurationException.class)
    public void getFloatInRangeToBig() throws ConfigurationException {
        float f = dict.get("long", ConfigurationDictionary.inRange(1f, 1.01f));
    }

    @Test
    public void getDoubleInRange() throws ConfigurationException {
        double d = dict.get("double", ConfigurationDictionary.inRange(-5000.0, 5000.0));
        assertEquals(d, 43.21, 0.01);
    }

    @Test(expected = ConfigurationException.class)
    public void getDoubleInRangeToSmall() throws ConfigurationException {
        double d = dict.get("double", ConfigurationDictionary.inRange(2000.0, 2005.0));
    }

    @Test(expected = ConfigurationException.class)
    public void getDoubleInRangeToBig() throws ConfigurationException {
        double d = dict.get("double", ConfigurationDictionary.inRange(1.0, 1.01));
    }

    @Test(expected = AssertionError.class)
    public void getStringInRange() throws ConfigurationException {
        String s = dict.get("string", ConfigurationDictionary.inRange("A", "Z"));
    }

    @Test(expected = AssertionError.class)
    public void invalidRangeCheck() {
        ConfigurationDictionary.inRange(10, 8);
    }

    @Test(expected = AssertionError.class)
    public void invalidRangeCheck2() {
        ConfigurationDictionary.inRange(null, 8);
    }

    @Test(expected = AssertionError.class)
    public void invalidRangeCheck3() {
        ConfigurationDictionary.inRange(5, null);
    }

    @Test(expected = AssertionError.class)
    public void invalidRangeCheck4() {
        ConfigurationDictionary.inRange(null, null);
    }

    @Test
    public void getByteInSet() throws ConfigurationException {
        byte b = dict.get("byte", ConfigurationDictionary.inSet((byte) 1, (byte) 8, (byte) 17));
    }

    @Test(expected = ConfigurationException.class)
    public void getByteNotInSet() throws ConfigurationException {
        byte b = dict.get("byte", ConfigurationDictionary.inSet((byte) 1, (byte) 77, (byte) 17));
    }

    @Test
    public void getShortInSet() throws ConfigurationException {
        short s = dict.get("short", ConfigurationDictionary.inSet((short) 1, (short) 8, (short) 16));
    }

    @Test(expected = ConfigurationException.class)
    public void getShortNotInSet() throws ConfigurationException {
        short s = dict.get("short", ConfigurationDictionary.inSet((short) 1, (short) 77, (short) 17));
    }

    @Test
    public void getIntInSet() throws ConfigurationException {
        int i = dict.get("int", ConfigurationDictionary.inSet(1, 8, 16, 32, 77));
    }

    @Test(expected = ConfigurationException.class)
    public void getIntNotInSet() throws ConfigurationException {
        int i = dict.get("int", ConfigurationDictionary.inSet(1));
    }

    @Test
    public void getLongInSet() throws ConfigurationException {
        long l = dict.get("long", ConfigurationDictionary.inSet(64L));
    }

    @Test(expected = ConfigurationException.class)
    public void getLongNotInSet() throws ConfigurationException {
        long l = dict.get("long", ConfigurationDictionary.inSet(1L));
    }

    @Test
    public void getFloatInSet() throws ConfigurationException {
        float f = dict.get("float", ConfigurationDictionary.inSet(1f, 2f, 3f, 12.34f, 22f));
    }

    @Test(expected = ConfigurationException.class)
    public void getFloatNotInSet() throws ConfigurationException {
        float f = dict.get("float", ConfigurationDictionary.inSet(0f, 1000f));
    }

    @Test
    public void getDoubleInSet() throws ConfigurationException {
        double d = dict.get("double", ConfigurationDictionary.inSet(0.0, 43.21, 1.1));
    }

    @Test(expected = ConfigurationException.class)
    public void getDoubleNotInSet() throws ConfigurationException {
        double d = dict.get("double", ConfigurationDictionary.inSet(0.9, 1.1));
    }

    @Test
    public void getStringInSet() throws ConfigurationException {
        String s = dict.get("string", ConfigurationDictionary.inSet("A", "B", "D", "VOID", "E"));
    }

    @Test(expected = ConfigurationException.class)
    public void getStringNotInSet() throws ConfigurationException {
        String s = dict.get("string", ConfigurationDictionary.inSet("1", "2", "3"));
    }

    private enum IncludingVOID {A, B, C, D, E, F, G, VOID, H, I, J}

    @Test
    public void getEnumExisting() throws ConfigurationException {
        IncludingVOID i = dict.get("string", IncludingVOID.class);
    }

    private enum NotIncludingVOID {A, B, C, D, E, F, G, H, I, J}

    @Test(expected = ConfigurationException.class)
    public void getEnumNotExisting() throws ConfigurationException {
        NotIncludingVOID n = dict.get("string", NotIncludingVOID.class);
    }

    @Test
    public void getDefaultEnum() throws ConfigurationException {
        DataPointType type = dict.get("somemissingkey", DataPointType.BOOLEAN);
        assertEquals(type, DataPointType.BOOLEAN);
    }

    @Test
    public void customValidityChecker() throws ConfigurationException {
        dict.get("float", new ConfigurationDictionary.ValidityChecker<Float>() {
            @Override
            public Class getType() {
                return Float.class;
            }

            @Override
            public void validate(String key, Float value) throws ConfigurationException {
            }
        });
    }

    @Test(expected = ConfigurationException.class)
    public void customValidityCheckerFailing() throws ConfigurationException {
        dict.get("float", new ConfigurationDictionary.ValidityChecker<Float>() {
            @Override
            public Class getType() {
                return Float.class;
            }

            @Override
            public void validate(String key, Float value) throws ConfigurationException {
                throw new ConfigurationException("I do not want that value!");
            }
        });
    }

    @Test
    public void argumentConversionTest() throws ConfigurationException {
        String[] args = {"--int=1", "--float=2.2", "--string=test", "--enum=INTEGER"};
        ConfigurationDictionary dict = new ConfigurationDictionary(args);
        assertEquals((int) dict.get("int", Integer.class), 1);
        assertEquals(dict.get("float", Double.class), 2.2, 0.001);
        assertEquals(dict.get("string", String.class), "test");
        assertEquals(dict.get("enum", DataPointType.class), DataPointType.INTEGER);
    }

    @Test
    public void propertiesConversionTest() throws ConfigurationException, IOException {
        Properties properties = new Properties();
        properties.load(
            ConfigurationDictionaryTest.class.getClassLoader()
                .getResourceAsStream("ConfigurationDictionaryTest.properties"));

        ConfigurationDictionary dict = new ConfigurationDictionary(properties);
        assertEquals((int) dict.get("int", Integer.class), 1);
        assertEquals(dict.get("float", Double.class), 2.2, 0.001);
        assertEquals(dict.get("string", String.class), "test");
        assertEquals(dict.get("enum", DataPointType.class), DataPointType.FLOATING_POINT);
    }

    @Test
    public void saxAttributesConversionTest() throws ConfigurationException, IOException {
        Attributes attributes = new Attributes() {
            @Override
            public int getLength() {
                return 4;
            }

            @Override
            public String getURI(int index) {
                return null;
            }

            @Override
            public String getLocalName(int index) {
                return null;
            }

            @Override
            public String getQName(int index) {
                switch (index) {
                    case 0:
                        return "int";

                    case 1:
                        return "float";

                    case 2:
                        return "string";

                    case 3:
                        return "enum";

                    default:
                        return null;
                }
            }

            @Override
            public String getType(int index) {
                return null;
            }

            @Override
            public String getValue(int index) {
                switch (index) {
                    case 0:
                        return "1";

                    case 1:
                        return "2.2";

                    case 2:
                        return "test";

                    case 3:
                        return "FLOATING_POINT";

                    default:
                        return null;
                }
            }

            @Override
            public int getIndex(String uri, String localName) {
                return 0;
            }

            @Override
            public int getIndex(String qName) {
                return 0;
            }

            @Override
            public String getType(String uri, String localName) {
                return null;
            }

            @Override
            public String getType(String qName) {
                return null;
            }

            @Override
            public String getValue(String uri, String localName) {
                return null;
            }

            @Override
            public String getValue(String qName) {
                return null;
            }
        };

        ConfigurationDictionary dict = new ConfigurationDictionary(attributes);
        assertEquals((int) dict.get("int", Integer.class), 1);
        assertEquals(dict.get("float", Double.class), 2.2, 0.001);
        assertEquals(dict.get("string", String.class), "test");
        assertEquals(dict.get("enum", DataPointType.class), DataPointType.FLOATING_POINT);
    }

    @Test(expected = ConfigurationException.class)
    public void unsupportedType() throws ConfigurationException {
        dict.get("int", DataPoint.class);
    }

    @Test(expected = ConfigurationException.class)
    public void unsupportedTypeForString() throws ConfigurationException {
        dict.get("string", DataPoint.class);
    }

    @Test(expected = ConfigurationException.class)
    public void nullAsDefault() throws ConfigurationException {
        dict.get("string", (String) null);
    }

    @Test(expected = ConfigurationException.class)
    public void inRangeValidatingWithNull() throws ConfigurationException, IOException {
        ConfigurationDictionary.ValidityChecker<Integer> validator =
            ConfigurationDictionary.inRange(1, 100);
        validator.validate("test", null);
    }

    @Test
    public void containsTest() throws ConfigurationException, IOException {
        assertTrue(dict.contains("int"));
        assertFalse(dict.contains("somemissingkey"));
    }

    @Test
    public void toStringTest() {
        assertEquals(dict.toString(), "{\n" +
            "aBoolean: true\n" +
            "anotherBoolean: false\n" +
            "byte: 8\n" +
            "byteo: 9\n" +
            "double: 43.21\n" +
            "doubleo: 87.65\n" +
            "float: 12.34\n" +
            "floato: 56.78\n" +
            "int: 32\n" +
            "into: 33\n" +
            "long: 64\n" +
            "longo: 65\n" +
            "sBoolean1: true\n" +
            "sBoolean2: TRUE\n" +
            "sBoolean3: True\n" +
            "sBoolean4: false\n" +
            "sBoolean5: FALSE\n" +
            "sBoolean6: False\n" +
            "short: 16\n" +
            "shorto: 17\n" +
            "string: VOID\n" +
            "stringFloat: 1234.5678\n" +
            "stringInt: 64\n" +
            "}");
    }

    @Test(expected = ConfigurationException.class)
    public void invalidWithDefault() throws ConfigurationException {
        dict.get("string", 55);
    }
}
