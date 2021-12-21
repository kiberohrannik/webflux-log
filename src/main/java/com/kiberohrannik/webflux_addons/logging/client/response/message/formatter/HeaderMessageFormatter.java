package com.kiberohrannik.webflux_addons.logging.client.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import com.kiberohrannik.webflux_addons.logging.extractor.HeaderExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class HeaderMessageFormatter implements ResponseDataMessageFormatter {

    private final HeaderExtractor headerExtractor;


    @Override
    public Mono<ResponseData> addData(LoggingProperties loggingProperties,
                                      Mono<ResponseData> sourceMessage) {

        if (loggingProperties.isLogHeaders()) {
            return sourceMessage.map(source -> {
                String headersMessage = formatHeaderMessage(source.getResponse(), loggingProperties);
                return source.addToLogs(headersMessage);
            });
        }

        return sourceMessage;
    }


    private String formatHeaderMessage(ClientResponse response, LoggingProperties props) {
        StringBuilder sb = new StringBuilder("\nHEADERS: [ ");

        MultiValueMap<String, String> headersToLog = new LinkedMultiValueMap<>(response.headers().asHttpHeaders());
        headersToLog.remove(HttpHeaders.SET_COOKIE);

        if (props.getMaskedHeaders() != null) {
            headerExtractor.setMask(headersToLog, props.getMaskedHeaders());
        }
        headerExtractor.extractAll(headersToLog, sb);

        return sb.append("]").toString();
    }
}