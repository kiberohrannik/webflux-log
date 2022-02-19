package com.kv.webflux.logging.client.request.message;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.request.message.formatter.BodyClientRequestFormatter;
import com.kv.webflux.logging.client.request.message.formatter.RequestMetadataMessageFormatter;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public class BaseRequestMessageCreator implements RequestMessageCreator {

    private final LoggingProperties properties;
    private final List<RequestMetadataMessageFormatter> metadataFormatters;
    private final BodyClientRequestFormatter bodyFormatter;


    public BaseRequestMessageCreator(LoggingProperties properties,
                                     List<RequestMetadataMessageFormatter> metadataFormatters) {
        this(properties, metadataFormatters, null);
    }

    public BaseRequestMessageCreator(LoggingProperties properties,
                                     List<RequestMetadataMessageFormatter> metadataFormatters,
                                     BodyClientRequestFormatter bodyFormatter) {
        this.properties = properties;
        this.metadataFormatters = metadataFormatters;
        this.bodyFormatter = bodyFormatter;
    }


    @Override
    public Mono<String> createMessage(ClientRequest request) {
        StringBuilder messageBuilder = new StringBuilder("REQUEST: ")
                .append(request.method().name())
                .append(" ")
                .append(request.url());

        for (RequestMetadataMessageFormatter metadataFormatter : metadataFormatters) {
            messageBuilder.append(metadataFormatter.formatMessage(request, properties));
        }

        return bodyFormatter != null
                ? bodyFormatter.formatMessage(request, properties).map(str -> messageBuilder.append(str).toString())
                : Mono.just(messageBuilder.toString());
    }
}