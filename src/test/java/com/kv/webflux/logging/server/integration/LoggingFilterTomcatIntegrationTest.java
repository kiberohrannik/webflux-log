package com.kv.webflux.logging.server.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kv.webflux.logging.server.base.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = LoggingFilterTomcatIntegrationTest.TomcatConfig.class)
public class LoggingFilterTomcatIntegrationTest extends BaseIntegrationTest {

    @Test
    void logRequestResponse_usingTomcat() throws JsonProcessingException {
        verifyTestEndpointRequestSuccess();
    }


    @TestConfiguration
    public static class TomcatConfig {

        @Bean
        public ReactiveWebServerFactory reactiveWebServerFactory() {
            return new TomcatReactiveWebServerFactory();
        }
    }
}
