package com.kiberohrannik.webflux_addons.logging.server.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kiberohrannik.webflux_addons.logging.server.base.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = LoggingFilterJettyIntegrationTest.JettyConfig.class)
public class LoggingFilterJettyIntegrationTest extends BaseIntegrationTest {

    @Test
    void logRequestResponse_usingJetty() throws JsonProcessingException {
        verifyTestEndpointRequestSuccess();
    }


    @TestConfiguration
    public static class JettyConfig {

        @Bean
        public ReactiveWebServerFactory reactiveWebServerFactory() {
            return new JettyReactiveWebServerFactory();
        }
    }
}
