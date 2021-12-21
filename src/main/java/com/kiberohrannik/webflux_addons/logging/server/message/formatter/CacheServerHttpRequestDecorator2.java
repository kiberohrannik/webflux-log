package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.charset.Charset;

public final class CacheServerHttpRequestDecorator2 extends ServerHttpRequestDecorator {

    //    private final StringBuilder cachedBody = new StringBuilder();
//    private final Mono<DataBuffer> cachedBody = Mono.just(new DefaultDataBufferFactory().allocateBuffer());
    private DataBuffer cachedBody = new DefaultDataBufferFactory().allocateBuffer();

    CacheServerHttpRequestDecorator2(ServerHttpRequest delegate) {
        super(delegate);
    }


    @Override
    public Flux<DataBuffer> getBody() {
//        return ConnectableFlux.from(super.getBody());
        return super.getBody()
//                .doOnNext(this::cache);
                .map(dataBuffer -> {
                    writeToBuffer(dataBuffer);
                    return dataBuffer;
                });
    }


    public void writeToBuffer(DataBuffer dataBuffer) {
        cachedBody = cachedBody.write(dataBuffer);
    }


    public String getCachedBody() {
//        return cachedBody.toString();
//        try {
//            ByteArrayOutputStream result = new ByteArrayOutputStream();
//            result.writeBytes(cachedBody.asInputStream().readAllBytes());
//            return result.toString(Charset.defaultCharset());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        try {
            byte[] bytes = cachedBody.asInputStream().readAllBytes();
            return new String(bytes, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


//    private void cache(DataBuffer buffer) {
//        cachedBody.append(UTF_8.decode(buffer.asByteBuffer()).toString());
//    }
}