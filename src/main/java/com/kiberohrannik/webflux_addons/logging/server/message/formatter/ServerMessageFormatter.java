package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import org.springframework.web.server.ServerWebExchange;

public interface ServerMessageFormatter {

    String addData(ServerWebExchange exchange, LoggingProperties logProps, String source);
}
