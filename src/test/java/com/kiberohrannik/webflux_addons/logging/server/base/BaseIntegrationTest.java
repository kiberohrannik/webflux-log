package com.kiberohrannik.webflux_addons.logging.server.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.server.ServerLoggingFilterFactory;
import com.kiberohrannik.webflux_addons.logging.server.app.TestController;
import net.bytebuddy.utility.RandomString;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoConfiguration
@ContextConfiguration(classes = {
        BaseIntegrationTest.LoggingFilterConfig.class,
        TestController.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BaseIntegrationTest extends BaseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();


    protected WebClient createWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    protected void verifyTestEndpointRequestSuccess() throws JsonProcessingException {
        TestController.TestDto body = new TestController.TestDto(RandomString.make(40), RandomString.make());
        String bodyJson = objectMapper.writeValueAsString(body);

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

                .bodyValue(bodyJson)

                .retrieve()
                .bodyToMono(String.class)
                .block();

        assertEquals(body + TestController.RESPONSE_PREFIX, result);
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
}
