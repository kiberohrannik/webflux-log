package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.extractor.HeaderExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class HeaderMessageFormatter implements ServerMessageFormatter {

    private final HeaderExtractor headerExtractor;


    @Override
    public Mono<String> addData(ServerHttpRequest request,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        if (loggingProperties.isLogHeaders()) {
            return sourceMessage.map(source -> source.concat(formatHeaderMessage(request, loggingProperties)));
        }

        return sourceMessage;
    }


    private String formatHeaderMessage(ServerHttpRequest request, LoggingProperties props) {
        StringBuilder sb = new StringBuilder(" HEADERS: [ ");

        MultiValueMap<String, String> headersToLog = new LinkedMultiValueMap<>(request.getHeaders());

        if (props.getMaskedHeaders() != null) {
            headerExtractor.setMask(headersToLog, props.getMaskedHeaders());
        }
        headerExtractor.extractAll(headersToLog, sb);

        return sb.append("]").toString();
    }
}