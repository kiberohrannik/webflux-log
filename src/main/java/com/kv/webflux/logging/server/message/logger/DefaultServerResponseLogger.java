package com.kv.webflux.logging.server.message.logger;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.HttpStatusProvider;
import com.kv.webflux.logging.provider.TimeElapsedProvider;
import com.kv.webflux.logging.server.message.formatter.ServerMessageFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Supplier;

public final class DefaultServerResponseLogger implements ServerResponseLogger {

    private static final Log log = LogFactory.getLog(DefaultServerResponseLogger.class);

    private final LoggingProperties loggingProperties;
    private final List<ServerMessageFormatter> messageFormatters;

    private final HttpStatusProvider statusProvider = new HttpStatusProvider();
    private final TimeElapsedProvider timeProvider = new TimeElapsedProvider();


    public DefaultServerResponseLogger(LoggingProperties loggingProperties,
                                       List<ServerMessageFormatter> messageFormatters) {
        this.loggingProperties = loggingProperties;
        this.messageFormatters = messageFormatters;
    }


    @Override
    public ServerHttpResponse log(ServerWebExchange exchange, long exchangeStartTimeMillis) {
        Supplier<String> baseMessageSupplier = createNoBodyMessage(exchange, exchangeStartTimeMillis);

        if (loggingProperties.isLogBody()) {
            return new LoggingServerHttpResponseDecorator(exchange.getResponse(), baseMessageSupplier);

        } else {
            exchange.getResponse()
                    .beforeCommit(() -> Mono.fromRunnable(() -> log.info(baseMessageSupplier.get())));
            return exchange.getResponse();
        }
    }


    private Supplier<String> createNoBodyMessage(ServerWebExchange exchange, long exchangeStartTimeMillis) {
        return () -> {
            String status = statusProvider.createMessage(exchange.getResponse().getRawStatusCode());

            String dataMessage = "";
            for (ServerMessageFormatter formatter : messageFormatters) {
                dataMessage = formatter.addData(exchange, loggingProperties, dataMessage);
            }

            String timeElapsed = timeProvider.createMessage(System.currentTimeMillis() - exchangeStartTimeMillis);

            return "RESPONSE:".concat(timeElapsed).concat(status).concat(dataMessage);
        };
    }
}
