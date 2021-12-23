package com.kiberohrannik.webflux_addons.logging.server.app;

import com.kiberohrannik.webflux_addons.logging.server.base.BaseIntegrationTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
@ComponentScan(basePackageClasses = {
        BaseIntegrationTest.TestController.class,
        BaseIntegrationTest.LoggingFilterConfig.class,
})
@ActiveProfiles("test")
public class TestingApp {

    public static void main(String[] args) {
        SpringApplication.run(TestingApp.class, args);
    }
}