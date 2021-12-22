package com.kiberohrannik.webflux_addons.logging.server;

import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.provider.CookieProvider;
import com.kiberohrannik.webflux_addons.logging.provider.HeaderProvider;
import com.kiberohrannik.webflux_addons.logging.server.message.DefaultServerMessageCreator;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.request.CookieRequestMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.request.HeaderRequestMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.request.ReqIdRequestMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.server.message.formatter.ServerMessageFormatter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.WebFilter;

import java.util.List;

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

        List<ServerMessageFormatter> formatters = List.of(
                new ReqIdRequestMessageFormatter(),
                new HeaderRequestMessageFormatter(new HeaderProvider()),
                new CookieRequestMessageFormatter(new CookieProvider())
        );

        return new LoggingFilter(new DefaultServerMessageCreator(props, formatters));
    }
}
