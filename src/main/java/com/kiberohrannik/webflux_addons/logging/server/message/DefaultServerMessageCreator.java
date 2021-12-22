package com.kiberohrannik.webflux_addons.logging.server.message;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@RequiredArgsConstructor
public final class DefaultServerMessageCreator implements ServerMessageCreator {

    private final LoggingProperties loggingProperties;
    private final List<ServerMessageFormatter> serverMessageFormatters;


    @Override
    public String createForRequest(ServerWebExchange exchange) {
        String baseMessage = "REQUEST: ".concat(exchange.getRequest().getMethodValue()).concat(" ")
                .concat(exchange.getRequest().getURI().toString());

        for (ServerMessageFormatter formatter : serverMessageFormatters) {
            baseMessage = formatter.addData(exchange, loggingProperties, baseMessage);
        }

        return baseMessage;
    }

    @Override
    public String createForResponse(ServerWebExchange exchange, long timeElapsedMillis) {
//        String baseMessage = "RESPONSE:"
//                + " ELAPSED TIME: " + LoggingUtils.formatResponseTime(timeElapsedMillis)
//                + formatHttpStatusMessage(response.rawStatusCode());

        return null;
//        exchange.getResponse().getCookies()
    }
}
