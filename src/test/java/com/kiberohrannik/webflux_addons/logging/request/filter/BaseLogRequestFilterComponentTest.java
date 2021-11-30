package com.kiberohrannik.webflux_addons.logging.request.filter;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.kiberohrannik.webflux_addons.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.request.message.BaseRequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.request.message.RequestMessageCreator;
import com.kiberohrannik.webflux_addons.logging.request.message.formatter.*;
import com.kiberohrannik.webflux_addons.logging.stub.RequestMessageCreatorTestDecorator;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
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


    //FIXME

    @Disabled("fix this test!!!!!!")
    @ParameterizedTest
    @MethodSource({"getLoggingPropertiesWithReqId"})
    void logRequest_whenTrueInLoggingProperties_thenLog(LoggingProperties loggingProperties) {
        String requestBody = RandomString.make();

        List<RequestDataMessageFormatter> formatters = new ArrayList<>();
        formatters.add(new ReqIdMessageFormatter());
        formatters.add(new HeaderMessageFormatter());
        formatters.add(new CookieMessageFormatter());
        formatters.add(new BodyMessageFormatter());

        RequestMessageCreator messageCreator = new BaseRequestMessageCreator(loggingProperties, formatters);
        RequestMessageCreatorTestDecorator testDecorator = new RequestMessageCreatorTestDecorator(messageCreator,
                loggingProperties, requestBody);

        LogRequestFilter logRequestFilter = LogRequestFilterFactory.defaultFilter(testDecorator);

        WebClient webClient = WebClient.builder()
                .filter(logRequestFilter.logRequest())
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

        //FIXME fix test
        LoggingProperties withHeadersProps = LoggingProperties.builder().logHeaders(true).maskedHeaders(new String[]{"Authorization"}).build();
        LoggingProperties withCookiesProps = LoggingProperties.builder().logCookies(true).maskedCookies(new String[]{"Cookie-1"}).build();
        LoggingProperties withBodyProps = LoggingProperties.builder().logBody(true).build();

        return Stream.of(
                Arguments.of(onlyUrlProps),
                Arguments.of(withHeadersProps),
                Arguments.of(withCookiesProps),
                Arguments.of(withBodyProps)
        );
    }

    private static Stream<Arguments> getLoggingPropertiesWithReqId() {
        LoggingProperties nullIdPrefix = LoggingProperties.builder().logRequestId(true).build();
        LoggingProperties withIdPrefix = LoggingProperties.builder().logRequestId(true).requestIdPrefix("TSTS").build();

        return Stream.of(
                Arguments.of(nullIdPrefix),
                Arguments.of(withIdPrefix)
        );
    }
}
