package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.extractor.HeaderExtractor;
import com.kiberohrannik.webflux_addons.logging.server.message.DefaultServerMessageCreator;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.*;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.List;

@WebFluxTest(controllers = LoggingFilterComponentTest.TestController.class)
@Import(LoggingFilterComponentTest.LoggingFilterConfig.class)
public class LoggingFilterComponentTest extends BaseTest {

    @Autowired
    private WebTestClient testClient;


    @Test
//    @RepeatedTest(5)
    void test() {
        String reqBody = "{\"some\":\"body\"}";

        testClient.post()
                .uri("/test/endpoint")
                .header(HttpHeaders.AUTHORIZATION, RandomString.make())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)

                .cookie("Cookie-1", RandomString.make(10))
                .cookie("Cookie-1", RandomString.make(10))
                .cookie("Cookie-3", RandomString.make(5))
                .bodyValue("{\"some\":\"body\"}")
//                .bodyValue("{\"name\":\"zhengtao\",\"age\":18,\"isHandsome\":true,\"email\":\"hj_zhengt@163.com\",\"timestamp\":1598963565962,\"oneDate\":\"2009-09-01 14:22:41\",\"birthday\":\"1991-10-01\",\"address\":{\"country\":\"China\",\"province\":\"AnHui\",\"city\":\"MingGuang\"},\"hobbys\":[{\"order\":\"first\",\"name\":\"coding\"},{\"order\":\"second\",\"name\":\"fishing\"},{\"order\":\"third\",\"name\":\"whatever\"}]}")

                .exchange()
//                .expectStatus().isOk()
                .expectStatus().isEqualTo(220)
                .returnResult(String.class)
                .getResponseBody()
                .single()
                .doOnNext(body -> Assertions.assertEquals(reqBody + "-RESPONSE", body))
                .block();
    }


    @TestConfiguration
    public static class LoggingFilterConfig {

        @Bean
        public WebFilter loggingFilter() {
            LoggingProperties props = LoggingProperties.builder()
                    .logRequestId(true)
                    .logHeaders(true)
                    .logCookies(true)
                    .logBody(true)
                    .build();

            List<ServerMessageFormatter> formatters = List.of(
                    new ReqIdMessageFormatter(),
                    new HeaderMessageFormatter(new HeaderExtractor()),
                    new CookieMessageFormatter(),
                    new BodyMessageFormatter()
            );

            return new LoggingFilter(new DefaultServerMessageCreator(props, formatters));
        }

//        @Order(value = Integer.MAX_VALUE)
//        @Bean
//        public WebFilter loggingFilter2() {
//            LoggingProperties props = LoggingProperties.builder()
//                    .logRequestId(true)
//                    .logHeaders(true)
//                    .logCookies(true)
//                    .logBody(true)
//                    .build();
//
//            List<ServerMessageFormatter> formatters = List.of(
//                    new ReqIdMessageFormatter(),
//                    new HeaderMessageFormatter(new HeaderExtractor()),
//                    new CookieMessageFormatter(),
//                    new BodyMessageFormatter()
//            );
//
//            return new LoggingFilter2(new DefaultServerMessageCreator(props, formatters));
//        }

//        @Bean
//        public HttpHandler loggingHandler() {
//            return new LoggingHandler();
//        }
    }

    @RestController
    @EnableWebFlux
    @SpringBootConfiguration
    public static class TestController {

        @PostMapping("/test/endpoint")
        public Mono<String> testEndpoint(@RequestBody Mono<String> requestBody, ServerWebExchange exchange) {
            exchange.getResponse().setRawStatusCode(220);
            return requestBody.doOnNext(Assertions::assertNotNull)
                    .map(body -> {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return body.concat("-RESPONSE");
                    });
        }
    }
}
