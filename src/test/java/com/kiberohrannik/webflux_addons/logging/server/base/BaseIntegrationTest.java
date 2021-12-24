package com.kiberohrannik.webflux_addons.logging.server.base;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.server.ServerLoggingFilterFactory;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoConfiguration
@ContextConfiguration(classes = {
        BaseIntegrationTest.LoggingFilterConfig.class,
        BaseIntegrationTest.TestController.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BaseIntegrationTest extends BaseTest {

    protected static final String RESPONSE_PREFIX = "-RESPONSE";


    protected WebClient createWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    protected void verifyTestEndpointRequestSuccess() {
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

        assertEquals(body + RESPONSE_PREFIX, result);
    }


    @TestConfiguration
    public static class LoggingFilterConfig {

        @Bean
        public WebFilter loggingFilter() {
            LoggingProperties props = LoggingProperties.builder()
                    .logRequestId(true)
                    .requestIdPrefix("TEST-REQ-ID")
                    .logHeaders(true)
                    .logCookies(true)
                    .logBody(true)
                    .build();

            return ServerLoggingFilterFactory.defaultFilter(props, props);
        }
    }

//    @RestController
    public static class TestController {

        @PostMapping("/test/endpoint")
        public Mono<String> testEndpoint(@RequestBody Mono<String> requestBody) {
            return requestBody.doOnNext(Assertions::assertNotNull)
                    .map(body -> {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return body.concat(RESPONSE_PREFIX);
                    });
        }
    }
}
