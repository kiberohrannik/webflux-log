package com.kiberohrannik.webflux_addons.logging.response.message;

import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.response.message.formatter.ResponseDataMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class BaseResponseMessageCreator implements ResponseMessageCreator {

    private final LoggingProperties loggingProperties;
    private final List<ResponseDataMessageFormatter> messageFormatters;


    @Override
    public Mono<String> formatMessage(ClientResponse response) {
        String baseMessage = "RESPONSE: " + formatHttpStatusMessage(response.rawStatusCode());

        //reqid (logPrefix)
        //headers
        //cookies
        //body
        Mono<String> logMessage = Mono.just(baseMessage);

        for (ResponseDataMessageFormatter formatter : messageFormatters) {
            logMessage = formatter.addData(response, loggingProperties, logMessage);
        }

        return logMessage;
    }

    private String formatHttpStatusMessage(int rawStatusCode) {
        String rawStatusCodeStr = String.valueOf(rawStatusCode);

        HttpStatus httpStatus = HttpStatus.resolve(rawStatusCode);
        if (httpStatus != null) {
            return rawStatusCodeStr.concat(" ").concat(httpStatus.getReasonPhrase());
        }
        return rawStatusCodeStr;
    }
}