package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.server.message.ServerMessageCreator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.Charset;

@RequiredArgsConstructor
public class LoggingFilter implements WebFilter {

    private static final Log log = LogFactory.getLog(LoggingFilter.class);
    private final ServerMessageCreator serverMessageCreator;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startMillis = System.currentTimeMillis();

        ServerHttpRequest httpRequest = exchange.getRequest();
        final String httpUrl = httpRequest.getURI().toString();

        ServerHttpRequestDecorator loggingServerHttpRequestDecorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
            String requestBody = "";

            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody().doOnNext(dataBuffer -> {
                    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                        Channels.newChannel(byteArrayOutputStream).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                        requestBody = byteArrayOutputStream.toString(Charset.defaultCharset());
                        log.info("request body:" + requestBody);
                    } catch (IOException e) {
                        log.error("some" + e);
                    }
                });
            }
        };

        return chain.filter(exchange.mutate().request(loggingServerHttpRequestDecorator).build());
    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        long startMillis = System.currentTimeMillis();
//
//        return serverMessageCreator.createForRequest(exchange)
//                .doOnNext(requestData -> log.info(requestData.getLogMessage()))
//                .flatMap(requestData -> chain.filter(exchange.mutate().request(requestData.getRequest()).build())
             //   .then(chain.filter(exchange)
//                        .doFinally(signalType -> {
//                            log.info("Elapsed Time: " + (System.currentTimeMillis() - startMillis) + " ms");
//                            log.info(serverMessageCreator.createForResponse(exchange));
//                        }));
//    }
}