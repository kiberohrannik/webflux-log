package com.kiberohrannik.webflux_addons.logging.request.filter;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.kiberohrannik.webflux_addons.base.BaseComponentTest;
import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;

@SpringBootTest
@SpringBootConfiguration
public class LogRequestFilterComponentTest extends BaseComponentTest {

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


    @ParameterizedTest
    @MethodSource("getLogPropsWithReqId")
    void logRequest_whenReqIdParamIsTrue_thenLog(LoggingProperties loggingProperties) {
        WebClient webClient = createTestRequestLogWebClient(loggingProperties, null);

        WireMock.stubFor(WireMock.post(PATH)
                .willReturn(WireMock.status(200)));

        webClient.post()
                .uri(URL)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @ParameterizedTest
    @MethodSource("getLogPropsWithHeadersAndCookies")
    void logRequest_whenHeadersOrCookiesAreTrue_thenLog(LoggingProperties loggingProperties) {
        WebClient webClient = createTestRequestLogWebClient(loggingProperties, null);

        WireMock.stubFor(WireMock.post(PATH)
                .withHeader("Accept", equalTo("application/json"))
                .withHeader("Authorization", equalTo("Bearer 1234"))
                .withCookie("Cookie-1", equalTo("value1"))
                .withCookie("Cookie-2", equalTo("value2"))
                .willReturn(WireMock.status(200)));

        webClient.post()
                .uri(URL)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer 1234")
                .cookie("Cookie-1", "value1")
                .cookie("Cookie-2", "value2")
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @Test
    void logRequest_whenBodyParamIsTrueAndNoBody_thenLogEmpty() {
        LoggingProperties properties = LoggingProperties.builder().logBody(true).build();
        WebClient webClient = createTestRequestLogWebClient(properties, null);

        WireMock.stubFor(WireMock.get(PATH)
                .willReturn(WireMock.status(200)));

        webClient.get()
                .uri(URL)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @Test
    void logRequest_whenBodyParamIsTrue_thenLog() {
        LoggingProperties properties = LoggingProperties.builder().logBody(true).build();
        String requestBody = RandomString.make(40);
        WebClient webClient = createTestRequestLogWebClient(properties, requestBody);

        WireMock.stubFor(WireMock.post(PATH)
                .withRequestBody(equalTo(requestBody))
                .willReturn(WireMock.status(200)));

        webClient.post()
                .uri(URL)
                .bodyValue(requestBody)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @Test
    void logRequest_whenAllParamsAreTrue_thenLog() {
        LoggingProperties properties = LoggingProperties.builder()
                .logRequestId(true).requestIdPrefix("TEST-PREF")
                .logHeaders(true).maskedHeaders("Authorization")
                .logCookies(true).maskedCookies("Cookie-1")
                .logBody(true).build();

        String requestBody = RandomString.make(40);
        WebClient webClient = createTestRequestLogWebClient(properties, requestBody);

        WireMock.stubFor(WireMock.post(PATH)
                .withHeader("Accept", equalTo("application/json"))
                .withHeader("Authorization", equalTo("Bearer 1234"))
                .withCookie("Cookie-1", equalTo("value1"))
                .withCookie("Cookie-2", equalTo("value2"))
                .withRequestBody(equalTo(requestBody))
                .willReturn(WireMock.status(200)));

        webClient.post()
                .uri(URL)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer 1234")
                .cookie("Cookie-1", "value1")
                .cookie("Cookie-2", "value2")
                .bodyValue(requestBody)
                .retrieve()
                .toBodilessEntity()
                .block();
    }


    private static Stream<Arguments> getLogPropsWithReqId() {
        LoggingProperties nullIdPrefix = LoggingProperties.builder().logRequestId(true).build();
        LoggingProperties withIdPrefix = LoggingProperties.builder().logRequestId(true).requestIdPrefix("TSTS").build();

        return Stream.of(
                Arguments.of(nullIdPrefix),
                Arguments.of(withIdPrefix)
        );
    }

    private static Stream<Arguments> getLogPropsWithHeadersAndCookies() {
        LoggingProperties noMasked = LoggingProperties.builder().logHeaders(true).logCookies(true).build();
        LoggingProperties withMasked = LoggingProperties.builder()
                .logHeaders(true).maskedHeaders("Authorization")
                .logCookies(true).maskedCookies("Cookie-2")
                .build();

        return Stream.of(
                Arguments.of(noMasked),
                Arguments.of(withMasked)
        );
    }
}