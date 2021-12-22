package com.kiberohrannik.webflux_addons.logging.client;

import java.util.concurrent.TimeUnit;

public final class LoggingUtils {

    public static final String DEFAULT_MASK = "{masked}";
    public static final String NO_BODY_MESSAGE = "{no body}";


    public static String formatReqId(String logPrefix) {
        return logPrefix.replaceFirst("\\[", "").replaceFirst("]", "").replaceAll("\\s", "");
    }

    public static String formatResponseTime(long responseTimeMillis) {
        return responseTimeMillis < 1000
                ? responseTimeMillis + "ms"
                : TimeUnit.MILLISECONDS.toSeconds(responseTimeMillis) + "s";
    }
}