package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.server.app.TestController;
import com.kiberohrannik.webflux_addons.logging.server.base.BaseIntegrationTest;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Disabled;
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

    @Disabled
    @Test
    void logRequestResponse_usingNetty() {
        String body = RandomString.make(40);

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

                .bodyValue(body)

                .retrieve()
                .bodyToMono(String.class)
                .block();

        assertEquals(body + TestController.RESPONSE_PREFIX, result);
    }


    @TestConfiguration
    public static class NettyConfig {

        @Bean
        public ReactiveWebServerFactory reactiveWebServerFactory() {
            return new NettyReactiveWebServerFactory();
        }
    }
}