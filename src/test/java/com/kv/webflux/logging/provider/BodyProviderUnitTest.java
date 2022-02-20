package com.kv.webflux.logging.provider;

import com.kv.webflux.logging.base.BaseTest;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.util.FastByteArrayOutputStream;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static com.kv.webflux.logging.client.LoggingUtils.NO_BODY_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BodyProviderUnitTest extends BaseTest {

    private static final String expectedNoBodyMessage = wrapBody(NO_BODY_MESSAGE);

    private final BodyProvider provider = new BodyProvider();


    @Test
    void createBodyMessage_whenBodyMonoIsNotEmpty_thenReturnWrapped() {
        String body = RandomString.make();
        Mono<String> bodyMono = Mono.just(body);

        StepVerifier.create(provider.createBodyMessage(bodyMono))
                .expectNext(wrapBody(body))
                .verifyComplete();
    }

    @Test
    void createBodyMessage_whenBodyMonoIsEmpty_thenReturnWithNoBody() {
        Mono<String> emptyBodyMono = Mono.empty();

        StepVerifier.create(provider.createBodyMessage(emptyBodyMono))
                .expectNext(wrapBody(NO_BODY_MESSAGE))
                .verifyComplete();
    }

    @Test
    void createBodyMessage_whenByteStreamNotEmpty_thenReturnWithBody() throws IOException {
        FastByteArrayOutputStream bodyStream = new FastByteArrayOutputStream();
        bodyStream.write(RandomString.make().getBytes());

        String actual = provider.createBodyMessage(bodyStream);
        assertEquals(wrapBody(bodyStream.toString()), actual);
    }

    @Test
    void createWithBody_whenByteStreamIsEmpty_thenReturnNoBodyMessage() {
        FastByteArrayOutputStream bodyStream = new FastByteArrayOutputStream();

        String actual = provider.createBodyMessage(bodyStream);
        assertEquals(expectedNoBodyMessage, actual);
    }

    @Test
    void createNoBodyMessage_test() {
        String actual = provider.createNoBodyMessage();
        assertEquals(expectedNoBodyMessage, actual);
    }

    @Test
    void createBodyMessage_whenFullString_thenReturnWrapped() {
        String body = RandomString.make();
        String expected = wrapBody(body);

        String actual = provider.createBodyMessage(body);
        assertEquals(expected, actual);
    }

    @Test
    void createBodyMessage_whenEmptyOrNullString_thenReturnNoBody() {
        String nullBody = null;
        String emptyBody = "";

        assertEquals(expectedNoBodyMessage, provider.createBodyMessage(nullBody));
        assertEquals(expectedNoBodyMessage, provider.createBodyMessage(emptyBody));
    }


    private static String wrapBody(String body) {
        return " BODY: [ " + body + " ]";
    }
}