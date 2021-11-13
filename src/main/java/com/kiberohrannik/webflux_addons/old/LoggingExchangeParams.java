package com.kiberohrannik.webflux_addons.old;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoggingExchangeParams {

    public static final String LOG_REQID = "reqid";

    private boolean needLogBody = false;
    private String serviceIdPrefix = "";
}
