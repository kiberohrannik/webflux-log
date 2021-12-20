package com.kiberohrannik.webflux_addons.logging.client;

public final class LoggingUtils {

    public static final String DEFAULT_MASK = "{masked}";
    public static final String NO_BODY_MESSAGE = "{no body}";


    public static String extractReqId(String logPrefix) {
        return logPrefix.replaceFirst("\\[", "").replaceFirst("]", "").replaceAll("\\s", "");
    }
}