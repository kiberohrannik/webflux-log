package com.kiberohrannik.webflux_addons.logging.server.message;

import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.Charset;

public class LoggingServerHttpRequestDecorator extends ServerHttpRequestDecorator {

    private static final Log log = LogFactory.getLog(LoggingServerHttpRequestDecorator.class);
    private final String logMessage;


    public LoggingServerHttpRequestDecorator(ServerHttpRequest delegate, String sourceLogMessage) {
        super(delegate);
        logMessage = sourceLogMessage;
    }


    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody()
                .doOnNext(dataBuffer -> {
                    try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
                        Channels.newChannel(byteStream).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                        String bodyStr = byteStream.toString(Charset.defaultCharset());

                        log.info(addBodyMessage(logMessage, bodyStr));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }


    private String addBodyMessage(String source, String bodyStr) {
        return source.concat(" BODY: [ ")
                .concat(bodyStr.isEmpty() ? LoggingUtils.NO_BODY_MESSAGE : bodyStr)
                .concat(" ]");
    }
}