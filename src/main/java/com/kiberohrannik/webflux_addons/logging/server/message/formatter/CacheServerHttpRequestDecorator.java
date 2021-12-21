package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

public final class CacheServerHttpRequestDecorator extends ServerHttpRequestDecorator {

//    public CachedDataBuffer cachedDataBuffer = new CachedDataBuffer()

    CacheServerHttpRequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody()
                .map(dataBuffer -> new CachedDataBuffer(dataBuffer));
    }
}