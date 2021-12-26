package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ReqIdMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.request.CookieRequestMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.request.HeaderRequestMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.response.CookieResponseMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.response.HeaderResponseMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.server.message.logger.DefaultServerRequestLogger;
import com.kiberohrannik.webflux_addons.logging.server.message.logger.DefaultServerResponseLogger;
import com.kiberohrannik.webflux_addons.logging.server.message.logger.ServerRequestLogger;
import com.kiberohrannik.webflux_addons.logging.server.message.logger.ServerResponseLogger;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ServerLoggingFilterFactory {

    public static LoggingFilter defaultFilter(LoggingProperties requestLogProps,
                                              LoggingProperties responseLogProps) {

        List<ServerMessageFormatter> requestFormatters = List.of(
                new ReqIdMessageFormatter(),
                new HeaderRequestMessageFormatter(),
                new CookieRequestMessageFormatter()
        );

        List<ServerMessageFormatter> responseFormatters = List.of(
                new ReqIdMessageFormatter(),
                new HeaderResponseMessageFormatter(),
                new CookieResponseMessageFormatter()
        );

        return new LoggingFilter(
                new DefaultServerRequestLogger(requestLogProps, requestFormatters),
                new DefaultServerResponseLogger(responseLogProps, responseFormatters)
        );
    }

    public static LoggingFilter customFilter(ServerRequestLogger serverRequestLogger,
                                             ServerResponseLogger serverResponseLogger) {

        return new LoggingFilter(serverRequestLogger, serverResponseLogger);
    }
}