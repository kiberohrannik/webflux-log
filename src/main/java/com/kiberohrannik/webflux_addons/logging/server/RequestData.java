package com.kiberohrannik.webflux_addons.logging.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Getter
@AllArgsConstructor
public class RequestData {
    private ServerHttpRequest request;
    private String logMessage;
}