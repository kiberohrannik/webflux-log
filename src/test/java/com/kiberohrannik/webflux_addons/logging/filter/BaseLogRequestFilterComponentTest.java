package com.kiberohrannik.webflux_addons.logging.filter;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.kiberohrannik.webflux_addons.logging.stub.TestRequestMessageCreator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;

@SpringBootTest
@SpringBootConfiguration
public class BaseLogRequestFilterComponentTest {

    private static final int SERVER_PORT = 8088;
    private static final String PATH = "/some/test/path";
    private static final String URL = "http://localhost:" + SERVER_PORT + PATH;

    private static WireMockServer mockServer;

    @BeforeAll
    static void startMockServer() {
        WireMock.configureFor(SERVER_PORT);

        mockServer = new WireMockServer(SERVER_PORT);
        mockServer.start();
    }

    @AfterAll
    static void stopMockServer() {
        mockServer.stop();
    }

    //TODO remove duplication from tests

    @Test
    void logRequest_whenMinConfig_thenLogOnlyMethodAndUrl() {
        LoggingProperties onlyUrlProps = LoggingProperties.builder().build();

        WebClient webClient = WebClient.builder()
                .filter(new BaseLogRequestFilter(new TestRequestMessageCreator()).logRequest(onlyUrlProps))
                .build();

        WireMock.stubFor(WireMock.get(PATH)
                .withHeader("Accept", equalTo("application/json"))
                .withHeader("Authorization", equalTo("Bearer 1234"))
                .willReturn(WireMock.status(200)));

        webClient.get()
                .uri(URL)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer 1234")
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @Test
    void logRequest_whenNeedLogHeaders_thenLogUrlWithHeaders() {
        LoggingProperties withHeadersProps = LoggingProperties.builder().logHeaders(true).build();

        WebClient webClient = WebClient.builder()
                .filter(new BaseLogRequestFilter(new TestRequestMessageCreator()).logRequest(withHeadersProps))
                .build();

        WireMock.stubFor(WireMock.get(PATH)
                .withHeader("Accept", equalTo("application/json"))
                .withHeader("Authorization", equalTo("Bearer 1234"))
                .willReturn(WireMock.status(200)));

        webClient.get()
                .uri(URL)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer 1234")
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
