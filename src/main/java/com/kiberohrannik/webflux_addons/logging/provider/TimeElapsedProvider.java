package com.kiberohrannik.webflux_addons.logging.provider;

import java.util.concurrent.TimeUnit;

public class TimeElapsedProvider {

    public String createMessage(long startTimeMillis) {
        long timeElapsed = System.currentTimeMillis() - startTimeMillis;

        String responseTime = timeElapsed < 1000
                ? timeElapsed + "ms"
                : TimeUnit.MILLISECONDS.toSeconds(timeElapsed) + "s";

        return " ELAPSED TIME: " + responseTime;
    }
}