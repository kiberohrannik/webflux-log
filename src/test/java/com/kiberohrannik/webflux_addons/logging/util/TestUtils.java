package com.kiberohrannik.webflux_addons.logging.util;

import java.util.Random;

public class TestUtils {

    private static final Random random = new Random();


    public static String formatToLoggedReqId(String logPrefix, String customReqIdPrefix) {
        return customReqIdPrefix + "_" + formatToLoggedReqId(logPrefix);
    }

    public static String formatToLoggedReqId(String logPrefix) {
        return logPrefix.replaceFirst("\\[", "").replaceFirst("]", "").replaceAll("\\s", "");
    }

    public static <T> T getRandomEnumValue(Class<T> enumClass) {
        int randomOrdinal = random.nextInt(enumClass.getEnumConstants().length);
        return enumClass.getEnumConstants()[randomOrdinal];
    }
}