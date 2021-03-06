package io.github.satr.jweb.components.helpers;

import java.util.regex.Pattern;

public class StringHelper {
    private static Pattern intPattern = Pattern.compile("^-?\\d{1,10}$");
    private static Pattern doublePattern = Pattern.compile("^-?\\d{1,30}([\\.\\,]\\d{1,30})?$");

    public static boolean isInteger(String value) {
        return value != null && intPattern.matcher(value).matches();
    }

    /* Simplified version:
    * - no support of E-notation
    * - max 30 digits in integer and fractional parts
    * - separator sign corresponded to locale
    * - no group separator
    */
    public static boolean isDouble(String value) {
        return value != null && doublePattern.matcher(value).matches();
    }

    public static boolean isEmptyOrWhitespace(String value) {
        return value == null || value.trim().length() == 0;
    }
}
