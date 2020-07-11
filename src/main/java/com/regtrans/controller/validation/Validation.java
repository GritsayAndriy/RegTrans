package com.regtrans.controller.validation;

import java.util.regex.Pattern;

public class Validation {

    private static final Pattern pattern = Pattern.compile("([\\w\\s]+)", Pattern.UNICODE_CHARACTER_CLASS);
    private static final int LENGTH = 20;

    public static boolean isNumerical(String value) {
        if (!value.isEmpty()) {
            try {
                if (haveLetters(value)) {
                    return false;
                }
                Double.parseDouble(value);
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        return true;
    }

    public static boolean isLetters(String s) {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if ((!Character.isLetter(s.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean haveLetters(String s){
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if ((Character.isLetter(s.charAt(i)))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isModel(String value) {
        if (value.isEmpty()) {
            return true;
        }
        return pattern.matcher(value).matches();
    }

    public static boolean validateLength(String value) {
        return value.length() <= LENGTH;
    }

    public static String capitalize(String str) {
        if (!str.equals("")) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        } else return str;
    }

    public static String oneSpace(String value) {
        if (value.equals(" ")) {
            value = value.trim();
        }
        return value.replaceAll("\\s+", " ");
    }
}
