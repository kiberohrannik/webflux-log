package com.kv.webflux.logging.server.message.logger;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.HttpStatusProvider;
import com.kv.webflux.logging.provider.TimeElapsedProvider;
import com.kv.webflux.logging.server.message.formatter.ServerMetadataMessageFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Supplier;

public final class DefaultServerResponseLogger implements ServerResponseLogger {

    private static final Log log = LogFactory.getLog(DefaultServerResponseLogger.class);

    private final LoggingProperties properties;
    private final List<ServerMetadataMessageFormatter> messageFormatters;

    private final HttpStatusProvider statusProvider = new HttpStatusProvider();
    private final TimeElapsedProvider timeProvider = new TimeElapsedProvider();


    public DefaultServerResponseLogger(LoggingProperties properties,
                                       List<ServerMetadataMessageFormatter> messageFormatters) {
        this.properties = properties;
        this.messageFormatters = messageFormatters;
    }


    @Override
    public ServerHttpResponse log(ServerWebExchange exchange, long exchangeStartTimeMillis) {
        Supplier<String> baseMessageSupplier = createMetadataMessage(exchange, exchangeStartTimeMillis);

        if (properties.isLogBody()) {
            return new LoggingServerHttpResponseDecorator(exchange.getResponse(), baseMessageSupplier);

        } else {
            exchange.getResponse()
                    .beforeCommit(() -> Mono.fromRunnable(() -> log.info(baseMessageSupplier.get())));
            return exchange.getResponse();
        }
    }


    private Supplier<String> createMetadataMessage(ServerWebExchange exchange, long exchangeStartTimeMillis) {
        return () -> {
            String status = statusProvider.createMessage(exchange.getResponse().getRawStatusCode());
            StringBuilder metadata = new StringBuilder(status);

            for (ServerMetadataMessageFormatter formatter : messageFormatters) {
                metadata.append(formatter.formatMessage(exchange, properties));
            }

            String timeElapsed = timeProvider.createMessage(System.currentTimeMillis() - exchangeStartTimeMillis);

            return "RESPONSE:".concat(timeElapsed).concat(metadata.toString());
        };
    }
}
