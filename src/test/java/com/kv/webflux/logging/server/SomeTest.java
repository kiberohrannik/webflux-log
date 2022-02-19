package com.kv.webflux.logging.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kv.webflux.logging.server.app.TestController;
import com.kv.webflux.logging.server.app.TestDto;
import com.kv.webflux.logging.server.base.BaseIntegrationTest;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = SomeTest.NettyConfig.class)
public class SomeTest extends BaseIntegrationTest {

    @Test
    void logRequestResponse_usingNetty() throws JsonProcessingException {
        TestDto dto = new TestDto(RandomString.make(), RandomString.make());
        String requestBody = new ObjectMapper().writeValueAsString(dto);

        String result = createWebClient().post()
                .uri("/test/endpoint")

                .header(HttpHeaders.REFERER, RandomString.make())
                .header(HttpHeaders.AUTHORIZATION, RandomString.make())
                .header(HttpHeaders.COOKIE, RandomString.make())

                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)

                .cookie("Cookie-1", RandomString.make(10))
                .cookie("Cookie-1", RandomString.make(10))
                .cookie("Cookie-3", RandomString.make(5))

                .bodyValue(requestBody)

                .retrieve()
                .bodyToMono(String.class)
                .block();

        assertEquals(requestBody + TestController.RESPONSE_PREFIX, result);
    }


    @TestConfiguration
    public static class NettyConfig {

        @Bean
        public ReactiveWebServerFactory reactiveWebServerFactory() {
            return new NettyReactiveWebServerFactory();
        }
    }
}