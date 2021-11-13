package com.kiberohrannik.webflux_addons.temp;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.kiberohrannik.webflux_addons.logging.creator.RequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.filter.BaseLogRequestFilter;
import com.kiberohrannik.webflux_addons.logging.filter.LoggingProperties;
import com.kiberohrannik.webflux_addons.old.BaseLoggingFilter;
import com.kiberohrannik.webflux_addons.old.LoggingExchangeParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
    void test() {
//        WebClient.builder()
//                .filters() //Consumer<List<ExchangeFilterFunction>> filtersConsumer

        WireMock.stubFor(WireMock.post("/some")
                .withRequestBody(WireMock.containing(""))
                .willReturn(WireMock.status(200)));

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .filter(new BaseLogRequestFilter(new RequestMessageCreator())
                        .logRequest(LoggingProperties.builder()
                                .logHeaders(true)
                                .logCookies(true)
                                .logBody(true)
                                .build()))
                .build();

        System.out.println("\n\n");

        webClient.post()
                .uri("/some")

                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer gtrwghtuiwut542h0jutnui")

                .cookie("Cookie-1", "1111") //TODO add sensitive headers,  sensitive cookies (exceptions)
                .cookie("Cookie-2", "2222")

                .attribute("Attribute-1", "AAAA")
                .attribute("Attribute-2", "BBBB")

                .body(Mono.just("some body 1234567890 (Mono.just - String)"), String.class)
//                .body(Mono.just(new SomeDto("some body 1234567890 (Mono.just - SomeDto)")), SomeDto.class)
//                .body(Mono.fromSupplier(() -> "olala"), String.class)
//                .body(Mono.fromSupplier(() -> "olala"), new ParameterizedTypeReference<String>() {})
//                .bodyValue("some body 1234567890")

                //TODO
//                .body(BodyInserter)

                //TODO
//                .body(producer)

                .retrieve()
                .toBodilessEntity()
                .block();

        System.out.println("\n\n");
    }

    @Test
    void testOld() {
        WireMock.stubFor(WireMock.post("/some").willReturn(WireMock.status(200)));

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .filter(new BaseLoggingFilter().logRequest(LoggingExchangeParams.builder()
                        .needLogBody(true)
                        .serviceIdPrefix("vfda")
                        .build()))
                .build();

        System.out.println("\n\n");

        webClient.post()
                .uri("/some")

                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer gtrwghtuiwut542h0jutnui")

                .cookie("Cookie-1", "1111") //TODO add sensitive headers,  sensitive cookies (exceptions)
                .cookie("Cookie-2", "2222")

                .attribute("Attribute-1", "AAAA")
                .attribute("Attribute-2", "BBBB")

                .bodyValue("some body 1234567890")

                .retrieve()
                .toBodilessEntity()
                .block();

        System.out.println("\n\n");
    }

    @Test
    void test2() {
        WireMock.stubFor(WireMock.post("/some").willReturn(WireMock.status(200)));


        WebClient webClient = WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector())
                .baseUrl("http://localhost:8080")
//                .filter(new Excha)
                .filter(new BaseLoggingFilter().logRequest(LoggingExchangeParams.builder()
                        .needLogBody(true)
                        .serviceIdPrefix("vfda")
                        .build()))
                .build();

        System.out.println("\n\n");

        webClient.post()
                .uri("/some")

                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer gtrwghtuiwut542h0jutnui")

                .cookie("Cookie-1", "1111") //TODO add sensitive headers,  sensitive cookies (exceptions)
                .cookie("Cookie-2", "2222")

                .attribute("Attribute-1", "AAAA")
                .attribute("Attribute-2", "BBBB")

                .bodyValue("some body 1234567890")

                .retrieve()
                .toBodilessEntity()
                .block();

        System.out.println("\n\n");
    }

    @Data
    @AllArgsConstructor
    public static class SomeDto {
        private String name;
    }
}
