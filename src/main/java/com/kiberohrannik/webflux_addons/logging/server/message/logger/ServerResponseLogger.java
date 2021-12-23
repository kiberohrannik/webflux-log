package com.kiberohrannik.webflux_addons.logging.server.message.logger;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

public interface ServerResponseLogger {

    ServerHttpResponse log(ServerWebExchange exchange, long timeElapsedMillis);
}