package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.WebFilter;

@EnableWebFlux
@SpringBootApplication
public class TempApp {

    public static void main(String[] args) {
        SpringApplication.run(TempApp.class, args);
    }

    @Bean
    public WebFilter loggingFilter() {
        LoggingProperties props = LoggingProperties.builder()
                .logRequestId(true)
                .logHeaders(true)
                .logCookies(true)
                .logBody(true)
                .build();

        return ServerLoggingFilterFactory.defaultFilter(props, props);
    }
}
