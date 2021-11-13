package com.kiberohrannik.webflux_addons.old;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static com.kiberohrannik.webflux_addons.old.LoggingExchangeParams.LOG_REQID;

@Log4j2
public class ExchangeTimeLoggingFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
                .elapsed()
                .map(tuple -> {
                    log.info("ReqID: " + request.logPrefix() + " ElapsedTime: [" + tuple.getT1() + " ms]");
                    return tuple.getT2();
                })
                .contextWrite(this::setLoggingReqid);
    }


    private Context setLoggingReqid(Context currentContext) {
        if (ThreadContext.get(LOG_REQID) != null) {
            return currentContext.put(LOG_REQID, ThreadContext.get(LOG_REQID));

        } else {
            log.error("ThreadContext reqid  is NULL!");
            return currentContext;
        }
    }
}