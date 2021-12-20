package com.kiberohrannik.webflux_addons.logging.client.response.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.ClientResponse;

@Setter
@Getter
@AllArgsConstructor
public class ResponseData {

    private final ClientResponse response;
    private String logMessage;


    public ResponseData addToLogs(String logMessage) {
        this.logMessage += logMessage;
        return this;
    }
}
