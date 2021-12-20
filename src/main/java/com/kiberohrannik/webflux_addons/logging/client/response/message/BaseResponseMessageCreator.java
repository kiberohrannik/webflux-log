package com.kiberohrannik.webflux_addons.logging.client.response.message;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.formatter.ResponseDataMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BaseResponseMessageCreator implements ResponseMessageCreator {

    private final LoggingProperties loggingProperties;
    private final List<ResponseDataMessageFormatter> messageFormatters;


    @Override
    public Mono<ResponseData> formatMessage(Long responseTimeMillis, ClientResponse response) {
        String baseMessage = "RESPONSE:"
                + formatResponseTimeMessage(responseTimeMillis)
                + formatHttpStatusMessage(response.rawStatusCode());

        Mono<ResponseData> logData = Mono.just(new ResponseData(response, baseMessage));
        for (ResponseDataMessageFormatter formatter : messageFormatters) {
            logData = formatter.addData(loggingProperties, logData);
        }

        return logData;
    }


    private String formatResponseTimeMessage(long responseTimeMillis) {
        String timeStr = responseTimeMillis < 1000
                ? responseTimeMillis + "ms"
                : TimeUnit.MICROSECONDS.toSeconds(responseTimeMillis) + "s";

        return " ELAPSED TIME: " + timeStr;
    }

    private String formatHttpStatusMessage(int rawStatusCode) {
        String msg = " STATUS: " + rawStatusCode;

        HttpStatus httpStatus = HttpStatus.resolve(rawStatusCode);
        if (httpStatus != null) {
            msg += " " + httpStatus.getReasonPhrase();
        }
        return msg;
    }
}