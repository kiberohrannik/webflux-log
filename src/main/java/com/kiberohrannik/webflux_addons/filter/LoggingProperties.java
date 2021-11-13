package com.kiberohrannik.webflux_addons.filter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class LoggingProperties {

    private boolean logHeaders = false;
    private boolean logBody = false;
}