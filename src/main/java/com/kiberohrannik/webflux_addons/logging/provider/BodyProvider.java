package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.util.FastByteArrayOutputStream;

public final class BodyProvider {

    public String createWithBody(FastByteArrayOutputStream bodyOutputStream) {
        String bodyStr = bodyOutputStream.toString();
        return " BODY: [ "
                .concat(bodyStr.isEmpty() ? LoggingUtils.NO_BODY_MESSAGE : bodyStr)
                .concat(" ]");
    }

    public String createWithEmptyBody() {
        return " BODY: [ "
                .concat(LoggingUtils.NO_BODY_MESSAGE)
                .concat(" ]");
    }
}
