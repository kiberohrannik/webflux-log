package com.kv.webflux.logging.server;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.server.message.formatter.ReqIdServerFormatter;
import com.kv.webflux.logging.server.message.formatter.ServerMetadataMessageFormatter;
import com.kv.webflux.logging.server.message.formatter.request.CookieServerRequestFormatter;
import com.kv.webflux.logging.server.message.formatter.request.HeaderServerRequestFormatter;
import com.kv.webflux.logging.server.message.formatter.response.CookieServerResponseFormatter;
import com.kv.webflux.logging.server.message.formatter.response.HeaderServerResponseFormatter;
import com.kv.webflux.logging.server.message.logger.DefaultServerRequestLogger;
import com.kv.webflux.logging.server.message.logger.DefaultServerResponseLogger;
import com.kv.webflux.logging.server.message.logger.ServerRequestLogger;
import com.kv.webflux.logging.server.message.logger.ServerResponseLogger;

import java.util.List;

public class ServerLoggingFilterFactory {

    public static LoggingFilter defaultFilter(LoggingProperties requestProperties,
                                              LoggingProperties responseProperties) {

        List<ServerMetadataMessageFormatter> requestFormatters = List.of(
                new ReqIdServerFormatter(),
                new HeaderServerRequestFormatter(),
                new CookieServerRequestFormatter()
        );

        List<ServerMetadataMessageFormatter> responseFormatters = List.of(
                new ReqIdServerFormatter(),
                new HeaderServerResponseFormatter(),
                new CookieServerResponseFormatter()
        );

        return new LoggingFilter(
                new DefaultServerRequestLogger(requestProperties, requestFormatters),
                new DefaultServerResponseLogger(responseProperties, responseFormatters)
        );
    }

    public static LoggingFilter customFilter(ServerRequestLogger serverRequestLogger,
                                             ServerResponseLogger serverResponseLogger) {
        return new LoggingFilter(serverRequestLogger, serverResponseLogger);
    }
}