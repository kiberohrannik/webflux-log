package com.kv.webflux.logging.provider;

import com.kv.webflux.logging.client.LoggingUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;

public final class BodyProvider {

    public String createBodyMessage(FastByteArrayOutputStream bodyOutputStream) {
        return createBodyMessage(bodyOutputStream.toString());
    }

    public String createBodyMessage(DataBuffer bodyDataBuffer) {
        return createBodyMessage(bodyDataBuffer.toString(Charset.defaultCharset()));
    }

    public String createBodyMessage(String body) {
        return StringUtils.hasLength(body) ? create(body) : createNoBodyMessage();
    }

    public String createNoBodyMessage() {
        return create(LoggingUtils.NO_BODY_MESSAGE);
    }

    private String create(String body) {
        return " BODY: [ ".concat(body).concat(" ]");
    }
}
