package com.kiberohrannik.webflux_addons.logging.server.message;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class BaseServerMessageCreator implements ServerMessageCreator {

    private final LoggingProperties loggingProperties;
    private final List<ServerMessageFormatter> serverMessageFormatters;


    @Override
    public Mono<String> formatMessage(ServerHttpRequest request) {
        String baseMessage = "REQUEST: ".concat(request.getMethodValue()).concat(" ")
                .concat(request.getURI().toString());

        Mono<String> logMessage = Mono.just(baseMessage);

        for (ServerMessageFormatter formatter : serverMessageFormatters) {
            logMessage = formatter.addData(request, loggingProperties, logMessage);
        }

        return logMessage;
    }
}
