package com.kiberohrannik.webflux_addons.logging;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class LoggingProperties {

    public static final String DEFAULT_MASK = "{masked}";

    private final boolean logHeaders;
    private final String[] maskedHeaders;

    private final boolean logCookies;
    private final String[] maskedCookies;

    private final boolean logBody;

    private final boolean logRequestId;
    private final String requestIdPrefix;
}