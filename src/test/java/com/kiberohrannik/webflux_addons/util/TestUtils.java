package com.kiberohrannik.webflux_addons.util;

import com.kiberohrannik.webflux_addons.logging.LoggingUtils;

public class TestUtils {

    public static String formatToLoggedReqId(String logPrefix, String customReqIdPrefix) {
        return customReqIdPrefix + "_" + formatToLoggedReqId(logPrefix);
    }

    public static String formatToLoggedReqId(String logPrefix) {
        return LoggingUtils.extractReqId(logPrefix);
    }
}