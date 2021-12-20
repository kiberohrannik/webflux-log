package com.kiberohrannik.webflux_addons.logging.client.base;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBootConfiguration
public abstract class BaseMockServerTest extends BaseComponentTest {

    private static final int SERVER_PORT = 8088;
    protected static final String PATH = "/some/test/path";
    protected static final String URL = "http://localhost:" + SERVER_PORT + PATH;

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
}
