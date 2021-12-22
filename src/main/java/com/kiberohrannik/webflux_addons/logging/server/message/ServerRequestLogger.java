package com.kiberohrannik.webflux_addons.logging.server.message;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

public interface ServerRequestLogger {

    ServerHttpRequest log(ServerWebExchange exchange);
}