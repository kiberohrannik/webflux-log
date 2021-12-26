package com.kiberohrannik.webflux_addons.logging.client.response.message;

import org.springframework.web.reactive.function.client.ClientResponse;


public class ResponseData {

    private final ClientResponse response;
    private String logMessage;


    public ResponseData(ClientResponse response, String logMessage) {
        this.response = response;
        this.logMessage = logMessage;
    }


    public ResponseData addToLogs(String logMessage) {
        this.logMessage += logMessage;
        return this;
    }

    public ClientResponse getResponse() {
        return response;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
