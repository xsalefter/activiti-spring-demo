package com.baeldung.activitispringdemo.util;

public class StringUtils {

    public static final boolean nullOrEmpty(final String s) {
        if (s == null) return false;
        return s.isEmpty();
    }
}
