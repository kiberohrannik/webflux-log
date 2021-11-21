package com.kiberohrannik.webflux_addons.logging;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class LoggingProperties {

    private final boolean logHeaders;//sensitive
    private final boolean logCookies; //sensitive
    private final boolean logBody;

    private final boolean logRequestId;
    private final String requestIdPrefix;
}