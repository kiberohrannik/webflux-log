package com.kiberohrannik.webflux_addons.logging.server.message.formatter;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.client.LoggingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class BodyMessageFormatter implements ServerMessageFormatter {

    @Override
    public Mono<String> addData(ServerWebExchange exchange,
                                LoggingProperties loggingProperties,
                                Mono<String> sourceMessage) {

        ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest());

        if (loggingProperties.isLogBody()) {
            return sourceMessage.flatMap(source -> addBody(decorator, source));
        }

        return sourceMessage;
    }


    private Mono<String> addBody(ServerHttpRequest request, String source) {
//        CacheServerHttpRequestDecorator requestDecorator = new CacheServerHttpRequestDecorator(request);

        return request.getBody()
                .single()
                .map(dataBuffer -> {
                    System.out.println("\n ==== \n");
                    return new CachedDataBuffer(dataBuffer);
                })
//                .map(cacheDataBuffer -> new String(cacheDataBuffer.getCachedBuffer().asByteBuffer().array(), Charset.defaultCharset()))
                .map(cachedDataBuffer -> {
                    DataBuffer cached  = cachedDataBuffer.getCachedBuffer();
                    byte[] bytes = new byte[cached.readableByteCount()];
                    cached.read(bytes);

                    DataBufferUtils.release(cached);
                    String s = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("s = " + s);
                    return s;
                })
//                .collect(Collectors.joining())
                .switchIfEmpty(Mono.just(LoggingUtils.NO_BODY_MESSAGE))
                .map(bodyStr -> source.concat("\nBODY: [ ").concat(bodyStr).concat(" ]"));
    }
}