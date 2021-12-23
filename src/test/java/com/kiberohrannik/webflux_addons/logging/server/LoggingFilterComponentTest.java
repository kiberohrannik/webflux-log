package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.server.base.BaseIntegrationTest;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.time.Duration;

@WebFluxTest(controllers = BaseIntegrationTest.TestController.class)
@Import({BaseIntegrationTest.LoggingFilterConfig.class, LoggingFilterComponentTest.TestConfig.class})
public class LoggingFilterComponentTest extends BaseTest {

    @Autowired
    private WebTestClient testClient;


    @Test
    void test() {
        String reqBody = "{\"some\":\"body\"}";

        testClient
                .mutate()
                .responseTimeout(Duration.ofSeconds(30))
                .build()

                .post()
                .uri("/test/endpoint")
                .header(HttpHeaders.AUTHORIZATION, RandomString.make())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)

                .cookie("Cookie-1", RandomString.make(10))
                .cookie("Cookie-1", RandomString.make(10))
                .cookie("Cookie-3", RandomString.make(5))
                .bodyValue("{\"some\":\"body\"}")

                .exchange()
//                .expectStatus().isOk()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .single()
                .doOnNext(body -> Assertions.assertEquals(reqBody + "-RESPONSE", body))
                .block();
    }


    @SpringBootConfiguration
    @EnableWebFlux
    static class TestConfig {

//        @Bean
//        public ReactiveWebServerFactory reactiveWebServerFactory() {
//            return new NettyReactiveWebServerFactory();
//        }
    }
}