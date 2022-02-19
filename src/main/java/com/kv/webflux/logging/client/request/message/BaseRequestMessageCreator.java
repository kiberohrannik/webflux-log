package com.kv.webflux.logging.client.request.message;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.request.message.formatter.RequestDataMessageFormatter;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class BaseRequestMessageCreator implements RequestMessageCreator {

    private final LoggingProperties properties;
    private final List<RequestDataMessageFormatter> formatters;


    public BaseRequestMessageCreator(LoggingProperties properties,
                                     List<RequestDataMessageFormatter> formatters) {
        this.properties = properties;
        this.formatters = formatters;
    }


    @Override
    public Mono<String> formatMessage(ClientRequest request) {
        String base = "REQUEST: "
                .concat(request.method().name())
                .concat(" ")
                .concat(request.url().toString());

        Flux<String> details = Flux.fromIterable(formatters)
                .flatMap(formatter -> formatter.addData(request, properties));

        return Mono.just(base)
                .concatWith(details)
                .reduce(String::concat);
    }
}