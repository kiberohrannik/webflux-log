package com.kiberohrannik.webflux_addons.logging.filter;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.kiberohrannik.webflux_addons.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.stub.TestRequestMessageCreator;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
public class BaseLogRequestFilterComponentTest extends BaseTest {

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
    @MethodSource("getLoggingProperties")
    void logRequest_whenTrueInLoggingProperties_thenLog(LoggingProperties loggingProperties) {
        String requestBody = RandomString.make();

        BaseLogRequestFilter logFilter = new BaseLogRequestFilter(new TestRequestMessageCreator(requestBody));
        WebClient webClient = WebClient.builder()
                .filter(logFilter.logRequest(loggingProperties))
                .build();

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


    private static Stream<Arguments> getLoggingProperties() {
        LoggingProperties onlyUrlProps = LoggingProperties.builder().build();
        LoggingProperties withHeadersProps = LoggingProperties.builder().logHeaders(true).build();
        LoggingProperties withCookiesProps = LoggingProperties.builder().logCookies(true).build();
        LoggingProperties withBodyProps = LoggingProperties.builder().logBody(true).build();

        return Stream.of(
                Arguments.of(onlyUrlProps),
                Arguments.of(withHeadersProps),
                Arguments.of(withCookiesProps),
                Arguments.of(withBodyProps)
        );
    }
}
