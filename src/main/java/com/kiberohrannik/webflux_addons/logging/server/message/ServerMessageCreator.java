package com.kiberohrannik.webflux_addons.logging.server.message;

import org.springframework.web.server.ServerWebExchange;

public interface ServerMessageCreator {

    String createForRequest(ServerWebExchange exchange);

    String createForResponse(ServerWebExchange exchange, long timeElapsedMillis);
}