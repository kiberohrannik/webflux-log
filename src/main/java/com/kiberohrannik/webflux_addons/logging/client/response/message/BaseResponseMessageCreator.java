package com.kiberohrannik.webflux_addons.logging.client.response.message;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.formatter.ResponseDataMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.provider.HttpStatusProvider;
import com.kiberohrannik.webflux_addons.logging.provider.TimeElapsedProvider;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public class BaseResponseMessageCreator implements ResponseMessageCreator {

    private final LoggingProperties loggingProperties;
    private final List<ResponseDataMessageFormatter> messageFormatters;

    private final HttpStatusProvider httpStatusProvider = new HttpStatusProvider();
    private final TimeElapsedProvider timeElapsedProvider = new TimeElapsedProvider();


    public BaseResponseMessageCreator(LoggingProperties loggingProperties,
                                      List<ResponseDataMessageFormatter> messageFormatters) {
        this.loggingProperties = loggingProperties;
        this.messageFormatters = messageFormatters;
    }


    @Override
    public Mono<ResponseData> formatMessage(Long responseTimeMillis, ClientResponse response) {
        String baseMessage = "RESPONSE:"
                + timeElapsedProvider.createMessage(responseTimeMillis)
                + httpStatusProvider.createMessage(response.rawStatusCode());

        Mono<ResponseData> logData = Mono.just(new ResponseData(response, baseMessage));
        for (ResponseDataMessageFormatter formatter : messageFormatters) {
            logData = formatter.addData(loggingProperties, logData);
        }

        return logData;
    }
}