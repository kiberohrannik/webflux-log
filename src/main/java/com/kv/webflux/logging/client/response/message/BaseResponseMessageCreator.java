package com.kv.webflux.logging.client.response.message;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.response.message.formatter.BodyClientResponseFormatter;
import com.kv.webflux.logging.client.response.message.formatter.ResponseMetadataMessageFormatter;
import com.kv.webflux.logging.provider.HttpStatusProvider;
import com.kv.webflux.logging.provider.TimeElapsedProvider;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public class BaseResponseMessageCreator implements ResponseMessageCreator {

    private final LoggingProperties properties;
    private final List<ResponseMetadataMessageFormatter> metadataFormatters;
    private final BodyClientResponseFormatter bodyFormatter;

    private final HttpStatusProvider httpStatusProvider = new HttpStatusProvider();
    private final TimeElapsedProvider timeElapsedProvider = new TimeElapsedProvider();


    public BaseResponseMessageCreator(LoggingProperties properties,
                                      List<ResponseMetadataMessageFormatter> metadataFormatters) {
        this.properties = properties;
        this.metadataFormatters = metadataFormatters;
        this.bodyFormatter = null;
    }

    public BaseResponseMessageCreator(LoggingProperties properties,
                                      List<ResponseMetadataMessageFormatter> metadataFormatters,
                                      BodyClientResponseFormatter bodyFormatter) {
        this.properties = properties;
        this.metadataFormatters = metadataFormatters;
        this.bodyFormatter = bodyFormatter;
    }


    @Override
    public Mono<ResponseData> formatMessage(Long responseTimeMillis, ClientResponse response) {
        StringBuilder logMessageBuilder = new StringBuilder("RESPONSE:");

        logMessageBuilder
                .append(timeElapsedProvider.createMessage(responseTimeMillis))
                .append(httpStatusProvider.createMessage(response.rawStatusCode()));

        for (ResponseMetadataMessageFormatter metadataFormatter : metadataFormatters) {
            logMessageBuilder.append(metadataFormatter.formatMessage(response, properties));
        }

        String logMessage = logMessageBuilder.toString();

        return bodyFormatter != null
                ? bodyFormatter.formatMessage(response, properties).map(data -> data.addFirst(logMessage))
                : Mono.just(new ResponseData(response, logMessage));
    }
}