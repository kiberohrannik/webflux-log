package com.kv.webflux.logging.client.response.message;

import org.springframework.web.reactive.function.client.ClientResponse;


public final class ResponseData {

    private final ClientResponse response;
    private String logMessage;


    public ResponseData(ClientResponse response, String logMessage) {
        this.response = response;
        this.logMessage = logMessage;
    }


    public ResponseData addFirst(String logMessage) {
        this.logMessage = logMessage + this.logMessage;
        return this;
    }

    public ClientResponse getResponse() {
        return response;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
