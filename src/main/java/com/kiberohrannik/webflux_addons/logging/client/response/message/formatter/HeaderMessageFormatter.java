package com.kiberohrannik.webflux_addons.logging.client.response.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.response.message.ResponseData;
import com.kiberohrannik.webflux_addons.logging.provider.HeaderProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

public class HeaderMessageFormatter implements ResponseDataMessageFormatter {

    private final HeaderProvider provider = new HeaderProvider();


    @Override
    public Mono<ResponseData> addData(LoggingProperties logProps, Mono<ResponseData> source) {
        if (logProps.isLogHeaders()) {
            return source.map(message -> {
                MultiValueMap<String, String> headersToLog =
                        new LinkedMultiValueMap<>(message.getResponse().headers().asHttpHeaders());

                headersToLog.remove(HttpHeaders.SET_COOKIE);

                String headersMessage = provider.createMessage(headersToLog, logProps);
                return message.addToLogs(headersMessage);
            });
        }

        return source;
    }
}