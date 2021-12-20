package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.extractor.HeaderExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class HeaderMessageFormatter implements RequestDataMessageFormatter {

    private final HeaderExtractor headerExtractor;


    @Override
    public Mono<String> addData(ClientRequest request,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        if (loggingProperties.isLogHeaders()) {
            return sourceMessage.map(source -> source.concat(formatHeaderMessage(request, loggingProperties)));
        }

        return sourceMessage;
    }


    private String formatHeaderMessage(ClientRequest request, LoggingProperties props) {
        StringBuilder sb = new StringBuilder(" HEADERS: [ ");

        MultiValueMap<String, String> headersToLog = new LinkedMultiValueMap<>(request.headers());

        if (props.getMaskedHeaders() != null) {
            headerExtractor.setMask(headersToLog, props.getMaskedHeaders());
        }
        headerExtractor.extractAll(headersToLog, sb);

        return sb.append("]").toString();
    }
}