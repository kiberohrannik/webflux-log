package com.kv.webflux.logging.server.message.formatter;

import com.kv.webflux.logging.client.LoggingProperties;
import org.springframework.web.server.ServerWebExchange;

public interface ServerMessageFormatter {

    String addData(ServerWebExchange exchange, LoggingProperties logProps, String source);
}
