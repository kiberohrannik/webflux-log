package com.kiberohrannik.webflux_addons.logging.client.request.message;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.request.message.formatter.RequestDataMessageFormatter;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public class BaseRequestMessageCreator implements RequestMessageCreator {

    private final LoggingProperties loggingProperties;
    private final List<RequestDataMessageFormatter> messageFormatters;


    public BaseRequestMessageCreator(LoggingProperties loggingProperties,
                                     List<RequestDataMessageFormatter> messageFormatters) {
        this.loggingProperties = loggingProperties;
        this.messageFormatters = messageFormatters;
    }


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