package com.kiberohrannik.webflux_addons.logging.server.message.logger;

import com.kiberohrannik.webflux_addons.logging.provider.BodyProvider;
import com.kiberohrannik.webflux_addons.logging.server.exception.DataBufferCopyingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.FastByteArrayOutputStream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.channels.Channels;
import java.util.function.Supplier;

public class LoggingServerHttpResponseDecorator extends ServerHttpResponseDecorator {

    private static final Log log = LogFactory.getLog(LoggingServerHttpResponseDecorator.class);

    private final BodyProvider provider = new BodyProvider();
    private final FastByteArrayOutputStream bodyOutputStream = new FastByteArrayOutputStream();


    public LoggingServerHttpResponseDecorator(ServerHttpResponse delegate, Supplier<String> sourceLogMessage) {
        super(delegate);

        delegate.beforeCommit(() -> {
            String bodyMessage = provider.createWithBody(bodyOutputStream);
            String fullLogMessage = sourceLogMessage.get().concat(bodyMessage);

            log.info(fullLogMessage);

            return Mono.empty();
        });
    }


    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        Flux<DataBuffer> bodyBufferWrapper = Flux.from(body)
                .map(dataBuffer -> copyBodyBuffer(bodyOutputStream, dataBuffer));

        return super.writeWith(bodyBufferWrapper);
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        Flux<Flux<DataBuffer>> bodyBufferWrapper = Flux.from(body)
                .map(publisher -> Flux.from(publisher)
                        .map(buffer -> copyBodyBuffer(bodyOutputStream, buffer)));

        return super.writeAndFlushWith(bodyBufferWrapper);
    }


    private DataBuffer copyBodyBuffer(FastByteArrayOutputStream bodyStream, DataBuffer buffer) {
        try {
            Channels.newChannel(bodyStream)
                    .write(buffer.asByteBuffer().asReadOnlyBuffer());

            return buffer;

        } catch (IOException e) {
            throw new DataBufferCopyingException(e);
        }
    }
}