package com.kv.webflux.logging.provider;

import java.util.concurrent.TimeUnit;

public class TimeElapsedProvider {

    public String createMessage(long timeElapsedMillis) {
        String responseTime = timeElapsedMillis < 1000
                ? timeElapsedMillis + "ms"
                : TimeUnit.MILLISECONDS.toSeconds(timeElapsedMillis) + "s";

        return " ELAPSED TIME: " + responseTime;
    }
}