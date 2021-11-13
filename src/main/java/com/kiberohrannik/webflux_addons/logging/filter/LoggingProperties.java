package com.kiberohrannik.webflux_addons.logging.filter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class LoggingProperties {

    private boolean logHeaders;
    private boolean logCookies;
    private boolean logBody;
}