package com.kv.webflux.logging.server.message.logger;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.provider.BodyProvider;
import com.kv.webflux.logging.provider.ReqIdProvider;
import com.kv.webflux.logging.server.exception.DataBufferCopyingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.FastByteArrayOutputStream;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.channels.Channels;

public class LoggingServerHttpRequestDecorator extends ServerHttpRequestDecorator {

    private static final Log log = LogFactory.getLog(LoggingServerHttpRequestDecorator.class);

    private final BodyProvider bodyProvider = new BodyProvider();
    private final String reqIdMessage;


    public LoggingServerHttpRequestDecorator(ServerHttpRequest delegate, LoggingProperties loggingProperties) {
        super(delegate);
        reqIdMessage = new ReqIdProvider().createFromLogId(delegate.getId(), loggingProperties);
    }


    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody()
                .switchIfEmpty(Flux.<DataBuffer>empty()
                        .doOnComplete(() -> log.info(reqIdMessage.concat(bodyProvider.createWithEmptyBody()))))

                .doOnNext(dataBuffer -> {
                    String fullBodyMessage = bodyProvider.createWithBody(copyBodyBuffer(dataBuffer));
                    log.info(reqIdMessage.concat(fullBodyMessage));
                });
    }


    private FastByteArrayOutputStream copyBodyBuffer(DataBuffer buffer) {
        try {
            FastByteArrayOutputStream bodyStream = new FastByteArrayOutputStream();
            Channels.newChannel(bodyStream)
                    .write(buffer.asByteBuffer().asReadOnlyBuffer());

            return bodyStream;

        } catch (IOException e) {
            throw new DataBufferCopyingException(e);
        }
    }
}