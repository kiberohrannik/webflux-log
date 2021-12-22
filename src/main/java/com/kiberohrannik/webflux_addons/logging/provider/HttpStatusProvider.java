package com.kiberohrannik.webflux_addons.logging.provider;

import org.springframework.http.HttpStatus;

public class HttpStatusProvider {

    public String createMessage(Integer rawStatusCode) {
        String msg = " STATUS: " + rawStatusCode;

        if (rawStatusCode != null) {
            HttpStatus httpStatus = HttpStatus.resolve(rawStatusCode);
            if (httpStatus != null) {
                msg += " " + httpStatus.getReasonPhrase();
            }
        }
        return msg;
    }
}
