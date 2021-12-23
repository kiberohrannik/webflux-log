package com.kiberohrannik.webflux_addons.logging.server.message.logger;

import com.kiberohrannik.webflux_addons.logging.provider.BodyProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

public class LoggingServerHttpRequestDecorator extends ServerHttpRequestDecorator {

    private static final Log log = LogFactory.getLog(LoggingServerHttpRequestDecorator.class);

    private final String logMessage;

    private final BodyProvider provider = new BodyProvider();


    public LoggingServerHttpRequestDecorator(ServerHttpRequest delegate, String sourceLogMessage) {
        super(delegate);
        logMessage = sourceLogMessage;
    }


    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody()
                .doOnNext(dataBuffer -> {
                    String bodyMessage = provider.createBodyMessage(dataBuffer);
                    log.info(logMessage.concat(bodyMessage));
                });
    }
}