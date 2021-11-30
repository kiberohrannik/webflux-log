package com.kiberohrannik.webflux_addons.util;

public class TestUtils {

    public static String formatToLoggedReqId(String logPrefix) {
        return logPrefix.replaceAll("[\\[\\]\\s]", "");
    }

    public static String formatToLoggedReqId(String logPrefix, String customReqIdPrefix) {
        return customReqIdPrefix + "_" + logPrefix.replaceAll("[\\[\\]\\s]", "");
    }
}