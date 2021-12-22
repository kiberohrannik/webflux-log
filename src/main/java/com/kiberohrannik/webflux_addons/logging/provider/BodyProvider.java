package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.springframework.core.io.buffer.DataBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.Charset;

public final class BodyProvider {

    public String createBodyMessage(DataBuffer bodyBuffer) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            Channels.newChannel(byteStream).write(bodyBuffer.asByteBuffer().asReadOnlyBuffer());
            String bodyStr = byteStream.toString(Charset.defaultCharset());

            return " BODY: [ "
                    .concat(bodyStr.isEmpty() ? LoggingUtils.NO_BODY_MESSAGE : bodyStr)
                    .concat(" ]");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
