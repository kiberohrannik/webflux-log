package com.kv.webflux.logging.server;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.server.message.formatter.ReqIdMessageFormatter;
import com.kv.webflux.logging.server.message.formatter.ServerMessageFormatter;
import com.kv.webflux.logging.server.message.formatter.request.CookieRequestMessageFormatter;
import com.kv.webflux.logging.server.message.formatter.request.HeaderRequestMessageFormatter;
import com.kv.webflux.logging.server.message.formatter.response.CookieResponseMessageFormatter;
import com.kv.webflux.logging.server.message.formatter.response.HeaderResponseMessageFormatter;
import com.kv.webflux.logging.server.message.logger.DefaultServerRequestLogger;
import com.kv.webflux.logging.server.message.logger.DefaultServerResponseLogger;
import com.kv.webflux.logging.server.message.logger.ServerRequestLogger;
import com.kv.webflux.logging.server.message.logger.ServerResponseLogger;

import java.util.List;

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