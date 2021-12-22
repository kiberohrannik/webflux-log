package com.kiberohrannik.webflux_addons.logging.server.message;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public final class DefaultServerMessageCreator implements ServerMessageCreator {

    private final LoggingProperties loggingProperties;
    private final List<ServerMessageFormatter> serverMessageFormatters;


    @Override
    public Mono<String> createForRequest(ServerWebExchange exchange) {
        String baseMessage = "REQUEST: ".concat(exchange.getRequest().getMethodValue()).concat(" ")
                .concat(exchange.getRequest().getURI().toString());

        Mono<String> logMessage = Mono.just(baseMessage);

        for (ServerMessageFormatter formatter : serverMessageFormatters) {
            logMessage = formatter.addData(exchange, loggingProperties, logMessage);
        }

        return logMessage;
    }

    @Override
    public String createForResponse(ServerWebExchange exchange) {
        exchange.getResponse().setComplete();
        String baseMessage = "RESPONSE:";
        return baseMessage + " " + exchange.getResponse().getHeaders() + " " + exchange.getResponse().getStatusCode()
                + " " + exchange.getAttributes();
    }
}
