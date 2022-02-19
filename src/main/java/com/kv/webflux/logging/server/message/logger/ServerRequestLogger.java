package com.kv.webflux.logging.server.message.logger;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

public interface ServerRequestLogger {

    ServerHttpRequest log(ServerWebExchange exchange);
}