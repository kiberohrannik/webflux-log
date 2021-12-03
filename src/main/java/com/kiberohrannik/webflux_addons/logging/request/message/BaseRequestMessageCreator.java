package com.kiberohrannik.webflux_addons.logging.request.message;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.request.message.formatter.RequestDataMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class BaseRequestMessageCreator implements RequestMessageCreator {

    private final LoggingProperties loggingProperties;
    private final List<RequestDataMessageFormatter> messageFormatters;


    @Override
    public Mono<String> formatMessage(ClientRequest request) {
        String baseMessage = "REQUEST: ".concat(request.method().name()).concat(" ").concat(request.url().toString());

        Mono<String> logMessage = Mono.just(baseMessage);

        for (RequestDataMessageFormatter formatter : messageFormatters) {
            logMessage = formatter.addData(request, loggingProperties, logMessage);
        }

        return logMessage;
    }
}