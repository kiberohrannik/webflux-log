package com.kiberohrannik.webflux_addons.logging.server.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TestController {

    public static final String TEST_ENDPOINT = "/test/endpoint";
    public static final String RESPONSE_PREFIX = "-RESPONSE";


    @PostMapping(TEST_ENDPOINT)
    public Mono<String> testEndpoint(@RequestBody Mono<TestDto> requestBody) {
        return requestBody.doOnNext(Assertions::assertNotNull)
                .map(body -> body.toString().concat(RESPONSE_PREFIX));
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestDto {
        private String value0;
        private String value1;
    }
}
