package com.kiberohrannik.webflux_addons.logging.server.message.logger;

import com.kiberohrannik.webflux_addons.logging.provider.BodyProvider;
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

public class LoggingServerHttpResponseDecorator extends ServerHttpResponseDecorator {

    private static final Log log = LogFactory.getLog(LoggingServerHttpResponseDecorator.class);

    private final String logMessage;

    private final BodyProvider provider = new BodyProvider();

    private final FastByteArrayOutputStream fastByteArrayOutputStream;


    public LoggingServerHttpResponseDecorator(ServerHttpResponse delegate, String sourceLogMessage) {
        super(delegate);
        logMessage = sourceLogMessage;

        fastByteArrayOutputStream = new FastByteArrayOutputStream();

        delegate.beforeCommit(() -> {
            log.info(logMessage + "  BODY:   " + fastByteArrayOutputStream.toString());

//            log.info(fastByteArrayOutputStream.toString());
            return Mono.empty();
        });
    }


//    @Override
//    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//        Mono<DataBuffer> buffer = Mono.from(body);
//
//        return super.writeWith(
//                buffer.doOnNext(dataBuffer -> {
//                    String bodyMessage = provider.createBodyMessage(dataBuffer);
//
//                    readBuffer(fastByteArrayOutputStream, dataBuffer);
//
//                    log.info(logMessage.concat(bodyMessage));
//                }));
//    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        Flux<DataBuffer> buffer = Flux.from(body)
                .map(dataBuffer -> {
                    readBuffer(fastByteArrayOutputStream, dataBuffer);
                    return dataBuffer;
                });

        return super.writeWith(buffer);
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return super.writeAndFlushWith(Flux.from(body).map(publisher ->
                Flux.from(publisher).map(buffer ->
                {
                    readBuffer(fastByteArrayOutputStream, buffer);
                    return buffer;
                })));
    }

    private void readBuffer(FastByteArrayOutputStream fastByteArrayOutputStream, DataBuffer buffer) {
        try {
            Channels.newChannel(fastByteArrayOutputStream)
                    .write(buffer.asByteBuffer().asReadOnlyBuffer());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}