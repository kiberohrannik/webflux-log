package com.kiberohrannik.webflux_addons.logging.server.message;

public interface TimeElapsedLogger {

    void log(long startTimeMillis, String logPrefix);
}