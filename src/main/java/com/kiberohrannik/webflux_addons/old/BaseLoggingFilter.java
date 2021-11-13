package com.kiberohrannik.webflux_addons.old;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.client.ClientRequest.LOG_ID_ATTRIBUTE;

@Log4j2
public class BaseLoggingFilter implements LoggingFilter {

    private final ClientRequestBodyExtractor bodyExtractor = new ClientRequestBodyExtractor();

    @Override
    public ExchangeFilterFunction logRequest(LoggingExchangeParams loggingParams) {
        return ExchangeFilterFunction.ofRequestProcessor(req -> {

            req = ClientRequest.from(req)
                    .attribute(LOG_ID_ATTRIBUTE, formatReqId(req.logPrefix(), loggingParams.getServiceIdPrefix()))
                    .build();

            log.info(log(req, loggingParams.isNeedLogBody()));
            return Mono.just(req);
        });
    }

    @Override
    public ExchangeFilterFunction logResponse(LoggingExchangeParams loggingParams) {
        return ExchangeFilterFunction.ofResponseProcessor(resp -> log(resp, loggingParams.isNeedLogBody()));
    }


    private String formatReqId(String logPrefix, String serviceIdPrefix) {
        return serviceIdPrefix.concat("-").concat(logPrefix.substring(1, logPrefix.length() - 2));
    }

    private String log(ClientRequest request, boolean needLogBody) {
        StringBuilder sb = new StringBuilder()
                .append(request.method().name()).append(" to ").append(request.url());

        addReqID(sb, request.logPrefix());
        addHeaders(sb, request.headers());

        if (needLogBody) {
            String bodyValue = (String) bodyExtractor.extractBody(request);
            sb.append(" Body: [").append(bodyValue).append("]");
        }

        return sb.toString();
    }

    private Mono<ClientResponse> log(ClientResponse response, boolean needLogBody) {
        StringBuilder sb = new StringBuilder();
        addStatusCode(sb, response);
        addReqID(sb, response.logPrefix());
        addHeaders(sb, response.headers().asHttpHeaders());

        if (!needLogBody) {
            log.info(sb.toString());
            return Mono.just(response);
        }

        return response.bodyToMono(String.class)
                .switchIfEmpty(Mono.just(""))
                .map(bodyStr -> {
                    log.info(sb.append(" Body: [ ").append(bodyStr).append(" ]").toString());
                    return bodyStr;
                })
                .map(body -> response.mutate().body(body).build());
    }

    private void addStatusCode(StringBuilder sb, ClientResponse response) {
        int rawStatus = response.rawStatusCode();
        sb.append("Returned rawStatus code ").append(rawStatus);

        HttpStatus status = HttpStatus.resolve(rawStatus);
        if (status != null) {
            sb.append(" ( ").append(status.getReasonPhrase()).append(" )");
        }
    }

    private void addReqID(StringBuilder sb, String logPrefix) {
        sb.append(" ReqID: ").append(logPrefix);
    }

    private void addHeaders(StringBuilder sb, HttpHeaders existingHeaders) {
        sb.append(" Headers: [ ");
        existingHeaders.forEach((name, values) -> values.forEach(
                value -> sb.append(name).append("=").append(value).append(" ")));
        sb.append("]");
    }
}