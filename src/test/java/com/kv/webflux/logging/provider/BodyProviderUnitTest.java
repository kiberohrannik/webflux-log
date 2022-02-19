package com.kv.webflux.logging.provider;

import com.kv.webflux.logging.base.BaseTest;
import com.kv.webflux.logging.client.LoggingUtils;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BodyProviderUnitTest extends BaseTest {

    private final BodyProvider provider = new BodyProvider();


    @Test
    void createWithBody_whenByteStreamNotEmpty_thenReturnWithBody() throws IOException {
        FastByteArrayOutputStream bodyStream = new FastByteArrayOutputStream();
        bodyStream.write(RandomString.make().getBytes());

        String actual = provider.createWithBody(bodyStream);
        log.info(actual);

        assertEquals(" BODY: [ " + bodyStream + " ]", actual);
    }

    @Test
    void createWithBody_whenByteStreamIsEmpty_thenReturnNoBodyMessage() {
        FastByteArrayOutputStream bodyStream = new FastByteArrayOutputStream();

        String actual = provider.createWithBody(bodyStream);
        log.info(actual);

        Assertions.assertEquals(" BODY: [ " + LoggingUtils.NO_BODY_MESSAGE + " ]", actual);
    }

    @Test
    void createWithEmptyBody_test() {
        String actual = provider.createWithEmptyBody();
        log.info(actual);

        assertEquals(" BODY: [ " + LoggingUtils.NO_BODY_MESSAGE + " ]", actual);
    }
}
