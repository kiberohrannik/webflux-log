package com.kiberohrannik.webflux_addons.logging.server.message.logger;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.provider.HttpStatusProvider;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@RequiredArgsConstructor
public final class DefaultServerResponseLogger implements ServerResponseLogger {

    private static final Log log = LogFactory.getLog(LoggingServerHttpRequestDecorator.class);

    private final LoggingProperties loggingProperties;
    private final List<ServerMessageFormatter> serverMessageFormatters;

    private final HttpStatusProvider httpStatusProvider = new HttpStatusProvider();


    @Override
    public ServerHttpResponse log(ServerWebExchange exchange, long timeElapsedMillis) {
        String baseMessage = "RESPONSE:"
                + httpStatusProvider.createMessage(exchange.getResponse().getRawStatusCode());

        for (ServerMessageFormatter formatter : serverMessageFormatters) {
            baseMessage = formatter.addData(exchange, loggingProperties, baseMessage);
        }

        if (loggingProperties.isLogBody()) {
            return new LoggingServerHttpResponseDecorator(exchange.getResponse(), baseMessage);

        } else {
            log.info(baseMessage);
            return exchange.getResponse();
        }
    }
}
