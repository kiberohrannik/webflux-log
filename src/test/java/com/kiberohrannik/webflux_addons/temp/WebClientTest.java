package com.kiberohrannik.webflux_addons.temp;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.request.filter.LogRequestFilterFactory;
import com.kiberohrannik.webflux_addons.logging.response.filter.LogResponseFilterFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@ActiveProfiles("test")
@SpringBootTest
@SpringBootConfiguration
public class WebClientTest {

    private static WireMockServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = new WireMockServer(8080);

        mockServer.start();
        WireMock.configureFor(8080);
    }

    @AfterEach
    void stop() {
        mockServer.stop();
    }


    @Test
    void test() throws InterruptedException {
        WireMock.stubFor(WireMock.post("/some")
                .withRequestBody(WireMock.containing(""))
                .willReturn(WireMock.status(200)
                        .withHeader("SomeHeader", "vfavfda")
                        .withHeader("Second-h", "12345")
                        .withBody("Some response body olala")));

        LoggingProperties loggingProperties = LoggingProperties.builder()
                .logHeaders(true)
                .logCookies(true)
                .logBody(true)
                .build();

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .filter(LogRequestFilterFactory.defaultFilter(loggingProperties).logRequest())
                .filter(LogResponseFilterFactory.defaultFilter(loggingProperties))
                .build();

        System.out.println("\n\n");

        String res = webClient.post()
                .uri("/some")

                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer gtrwghtuiwut542h0jutnui")

                .cookie("Cookie-1", "1111") //TODO add sensitive headers,  sensitive cookies (exceptions)
                .cookie("Cookie-2", "2222")

                .attribute("Attribute-1", "AAAA")
                .attribute("Attribute-2", "BBBB")

//                .body(Mono.just("some body 1234567890 (Mono.just - String)"), String.class)
//                .body(Mono.just(new SomeDto("some body 1234567890 (Mono.just - SomeDto)")), SomeDto.class)
//                .body(Mono.fromSupplier(() -> "olala -- fromSupplier"), String.class)
//                .body(Mono.fromSupplier(() -> "olala -- fromSupplier - ParameterizedTypeReference"), new ParameterizedTypeReference<String>() {})
//                .bodyValue("some body 1234567890")

                //TODO
                .body(BodyInserters.fromPublisher(Mono.just("some body 1234567890 -- BodyInserters"), String.class))
//                .body(BodyInserters.fromPublisher(Flux.just("val1", "val2", "val3"), String.class))

                //TODO
//                .body(producer)

                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("\n\n");

        TimeUnit.SECONDS.sleep(5);
    }


    @Data
    @AllArgsConstructor
    public static class SomeDto {
        private String name;
    }
}
