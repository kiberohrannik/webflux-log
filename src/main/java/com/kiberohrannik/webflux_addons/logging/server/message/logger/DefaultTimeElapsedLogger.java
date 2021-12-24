package com.kiberohrannik.webflux_addons.logging.server.message.logger;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.provider.ReqIdProvider;
import com.kiberohrannik.webflux_addons.logging.provider.TimeElapsedProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@RequiredArgsConstructor
public final class DefaultTimeElapsedLogger implements TimeElapsedLogger {

    private static final Log log = LogFactory.getLog(DefaultTimeElapsedLogger.class);

    private final LoggingProperties loggingProperties;

    private final TimeElapsedProvider timeElapsedProvider = new TimeElapsedProvider();
    private final ReqIdProvider reqIdProvider = new ReqIdProvider();


    @Override
    public void log(long startTimeMillis, String logPrefix) {
        String reqIdMessage = reqIdProvider.createFromLogPrefix(logPrefix, loggingProperties, "").trim();
        String elapsedTimeMessage = timeElapsedProvider.createMessage(System.currentTimeMillis() - startTimeMillis);

        log.info(reqIdMessage.concat(elapsedTimeMessage));
    }
}
