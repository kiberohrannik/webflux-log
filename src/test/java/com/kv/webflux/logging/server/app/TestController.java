package com.kv.webflux.logging.server.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ObjectMapper objectMapper;


    @PostMapping(TEST_ENDPOINT)
    public Mono<String> testEndpoint(@RequestBody Mono<TestDto> requestBody) {
        return requestBody.doOnNext(Assertions::assertNotNull)
                .map(body -> toJson(body) + RESPONSE_PREFIX);
    }


    private String toJson(TestDto testDto) {
        try {
            return objectMapper.writeValueAsString(testDto);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
