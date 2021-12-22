package com.kiberohrannik.webflux_addons.logging.client.util;

import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;

public class TestUtils {

    public static String formatToLoggedReqId(String logPrefix, String customReqIdPrefix) {
        return customReqIdPrefix + "_" + formatToLoggedReqId(logPrefix);
    }

    public static String formatToLoggedReqId(String logPrefix) {
        return logPrefix.replaceFirst("\\[", "").replaceFirst("]", "").replaceAll("\\s", "");
    }
}