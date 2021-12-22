package com.kiberohrannik.webflux_addons.logging.server.message;

import com.kiberohrannik.webflux_addons.logging.provider.BodyProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Mono;

public class LoggingServerHttpResponseDecorator extends ServerHttpResponseDecorator {

    private static final Log log = LogFactory.getLog(LoggingServerHttpResponseDecorator.class);

    private final String logMessage;

    private final BodyProvider provider = new BodyProvider();


    public LoggingServerHttpResponseDecorator(ServerHttpResponse delegate, String sourceLogMessage) {
        super(delegate);
        logMessage = sourceLogMessage;
    }


    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        Mono<DataBuffer> buffer = Mono.from(body);

        return super.writeWith(
                buffer.doOnNext(dataBuffer -> {
                    String bodyMessage = provider.createBodyMessage(dataBuffer);
                    log.info(logMessage.concat(bodyMessage));
                }));
    }
}