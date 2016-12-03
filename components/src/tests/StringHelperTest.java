package tests;


import io.github.satr.jweb.components.helpers.StringHelper;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringHelperTest {
    @Test
    public void isInteger() throws Exception {
        assertTrue(StringHelper.isInteger("" + Integer.MAX_VALUE));
        assertTrue(StringHelper.isInteger("" + Integer.MIN_VALUE));
        assertTrue(StringHelper.isInteger("00000123"));
        assertTrue(StringHelper.isInteger("123"));
        assertTrue(StringHelper.isInteger("-00000123"));
        assertTrue(StringHelper.isInteger("-123"));

        assertFalse(StringHelper.isInteger(""));
        assertFalse(StringHelper.isInteger("" + Integer.MAX_VALUE + "0"));
        assertFalse(StringHelper.isInteger("" + Integer.MIN_VALUE + "0"));
        assertFalse(StringHelper.isInteger("123.0"));
        assertFalse(StringHelper.isInteger("--123"));
        assertFalse(StringHelper.isInteger("+123"));
        assertFalse(StringHelper.isInteger(".0"));
        assertFalse(StringHelper.isInteger("abc"));
    }

    @Test
    public void isDouble() throws Exception {
        assertTrue(StringHelper.isDouble("11111111111111111111.11111111111111111111"));
        assertTrue(StringHelper.isDouble("-11111111111111111111.11111111111111111111"));
        assertTrue(StringHelper.isDouble("00000123"));
        assertTrue(StringHelper.isDouble("123"));
        assertTrue(StringHelper.isDouble("-00000123"));
        assertTrue(StringHelper.isDouble("-123"));
        assertTrue(StringHelper.isDouble("00000123.0001"));
        assertTrue(StringHelper.isDouble("123.12"));
        assertTrue(StringHelper.isDouble("123,12"));
        assertTrue(StringHelper.isDouble("-00000123.12"));
        assertTrue(StringHelper.isDouble("-123.12"));
        assertTrue(StringHelper.isDouble("-123,12"));
        assertTrue(StringHelper.isDouble("0.0"));
        assertTrue(StringHelper.isDouble(null));

        assertFalse(StringHelper.isDouble(""));
        assertFalse(StringHelper.isDouble("" + Double.MAX_VALUE + "0"));
        assertFalse(StringHelper.isDouble("" + Double.MIN_VALUE + "0"));
        assertFalse(StringHelper.isDouble("--123"));
        assertFalse(StringHelper.isDouble("+123"));
        assertFalse(StringHelper.isDouble("abc"));
        assertFalse(StringHelper.isDouble("123..0"));
        assertFalse(StringHelper.isDouble("--123"));
        assertFalse(StringHelper.isDouble("+123"));
        assertFalse(StringHelper.isDouble("123."));
        assertFalse(StringHelper.isDouble("..0"));
        assertFalse(StringHelper.isDouble("abc"));
        assertFalse(StringHelper.isDouble(null));
    }

}