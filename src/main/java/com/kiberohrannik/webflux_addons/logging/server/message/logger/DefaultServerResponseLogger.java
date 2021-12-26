package com.kiberohrannik.webflux_addons.logging.server.message.logger;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.provider.HttpStatusProvider;
import com.kiberohrannik.webflux_addons.logging.provider.TimeElapsedProvider;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public final class DefaultServerResponseLogger implements ServerResponseLogger {

    private static final Log log = LogFactory.getLog(DefaultServerResponseLogger.class);

    private final LoggingProperties loggingProperties;
    private final List<ServerMessageFormatter> serverMessageFormatters;

    private final HttpStatusProvider statusProvider = new HttpStatusProvider();
    private final TimeElapsedProvider timeProvider = new TimeElapsedProvider();


    @Override
    public ServerHttpResponse log(ServerWebExchange exchange, long exchangeStartTimeMillis) {
        Supplier<String> baseMessageSupplier = createNoBodyMessage(exchange, exchangeStartTimeMillis);

        if (loggingProperties.isLogBody()) {
            return new LoggingServerHttpResponseDecorator(exchange.getResponse(), baseMessageSupplier);

        } else {
            exchange.getResponse()
                    .beforeCommit(() -> Mono.<Void>fromRunnable(() -> log.info(baseMessageSupplier.get())));
            return exchange.getResponse();
        }
    }


    private Supplier<String> createNoBodyMessage(ServerWebExchange exchange, long exchangeStartTimeMillis) {
        return () -> {
            String status = statusProvider.createMessage(exchange.getResponse().getRawStatusCode());

            String dataMessage = "";
            for (ServerMessageFormatter formatter : serverMessageFormatters) {
                dataMessage = formatter.addData(exchange, loggingProperties, dataMessage);
            }

            String timeElapsed = timeProvider.createMessage(System.currentTimeMillis() - exchangeStartTimeMillis);

            return "RESPONSE:".concat(timeElapsed).concat(status).concat(dataMessage);
        };
    }
}
