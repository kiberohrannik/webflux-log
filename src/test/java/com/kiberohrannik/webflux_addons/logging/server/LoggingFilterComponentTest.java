package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.extractor.HeaderExtractor;
import com.kiberohrannik.webflux_addons.logging.server.message.BaseServerMessageCreator;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.HeaderMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.WebFilter;

import java.util.List;

@WebFluxTest
@Import(LoggingFilterComponentTest.LoggingFilterConfig.class)
public class LoggingFilterComponentTest extends BaseTest {

    @Autowired
    private WebTestClient testClient;


    @Test
    void test() {
        testClient.get()
                .uri("/random/path")
                .header(HttpHeaders.AUTHORIZATION, RandomString.make())
                .exchange()
                .expectStatus().isNotFound();
    }


    @SpringBootConfiguration
    public static class LoggingFilterConfig {

        @Bean
        public WebFilter loggingFilter() {
            LoggingProperties props = LoggingProperties.builder().logHeaders(true).build();
            List<ServerMessageFormatter> formatters = List.of(new HeaderMessageFormatter(new HeaderExtractor()));

            return new LoggingFilter(new BaseServerMessageCreator(props, formatters));
        }
    }
}
