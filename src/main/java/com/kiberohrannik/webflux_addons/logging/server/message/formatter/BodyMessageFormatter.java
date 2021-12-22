package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import com.kiberohrannik.webflux_addons.logging.server.RequestData;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.Charset;

@RequiredArgsConstructor
public final class BodyMessageFormatter implements ServerMessageFormatter {

    @Override
    public Mono<RequestData> addData(LoggingProperties loggingProperties, Mono<RequestData> sourceMessage) {

        if (loggingProperties.isLogBody()) {
//            return sourceMessage.flatMap(source -> addBody(exchange.getRequest(), source));
            return sourceMessage.flatMap(source -> {
                return addBody(source.getRequest(), source.getLogMessage());
            });
        }

        return sourceMessage;
    }


    private Mono<RequestData> addBody(ServerHttpRequest request, String source) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


        ServerHttpRequestDecorator loggingServerHttpRequestDecorator = new ServerHttpRequestDecorator(request) {

            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody().doOnNext(dataBuffer -> {
                    try {
                        Channels.newChannel(byteArrayOutputStream).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        };

        return loggingServerHttpRequestDecorator
                .getBody()
                .map(dataBuffer -> {
                        return new RequestData(loggingServerHttpRequestDecorator,
                                createMessage(source, byteArrayOutputStream.toString(Charset.defaultCharset())));

                })
                .singleOrEmpty()

//                .map(body -> new RequestData(request, createMessage(source, new CachedDataBuffer(body).getCachedContent())))
//                .map(CachedDataBuffer::getCachedContent)

                .switchIfEmpty(Mono.just(new RequestData(loggingServerHttpRequestDecorator, LoggingUtils.NO_BODY_MESSAGE)))
//                .map(bodyStr -> createMessage(source, bodyStr));
                ;
    }

//    private Mono<String> addBody(ServerHttpRequest request, String source) {
//        return request.getBody()
//                .singleOrEmpty()
//
//                .map(CachedDataBuffer::new)
//                .map(CachedDataBuffer::getCachedContent)
//
//                .switchIfEmpty(Mono.just(LoggingUtils.NO_BODY_MESSAGE))
//                .map(bodyStr -> createMessage(source, bodyStr));
//    }


    private String createMessage(String source, String bodyStr) {
        return source.concat(" BODY: [ ").concat(bodyStr).concat(" ]");
    }
}