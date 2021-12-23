package com.kiberohrannik.webflux_addons.logging.server.message.logger;

public interface TimeElapsedLogger {

    void log(long startTimeMillis, String logPrefix);
}