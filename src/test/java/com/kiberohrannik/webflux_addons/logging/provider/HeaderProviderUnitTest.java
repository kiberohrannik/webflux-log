package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.kiberohrannik.webflux_addons.logging.client.LoggingUtils.DEFAULT_MASK;
import static org.junit.jupiter.api.Assertions.*;

public class HeaderProviderUnitTest extends BaseTest {

    private final HeaderProvider provider = new HeaderProvider();


    @Test
    void createMessage_whenNoMasked_thenAddAll() {
        String headerName0 = RandomString.make();
        String headerValue0 = RandomString.make();

        String headerName1 = RandomString.make();
        String headerValue1 = RandomString.make();

        String headerValue2 = RandomString.make();

        LoggingProperties logProps = LoggingProperties.builder().build();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(headerName0, headerValue0);
        headers.add(headerName1, headerValue1);
        headers.add(headerName0, headerValue2);

        String actual = provider.createMessage(headers, logProps);
        log.info(actual);

        assertAll(
                () -> assertTrue(actual.contains(" HEADERS: [ ")),
                () -> assertTrue(actual.contains(headerName0 + "=" + headerValue0)),
                () -> assertTrue(actual.contains(headerName1 + "=" + headerValue1)),
                () -> assertTrue(actual.contains(headerName0 + "=" + headerValue2))
        );
    }

    @Test
    void createMessage_whenMasked_thenAddWithMask() {
        String headerName0 = RandomString.make();
        String headerValue0 = RandomString.make();

        String headerName1 = RandomString.make();
        String headerValue1 = RandomString.make();

        String headerName2 = RandomString.make();
        String headerValue2 = RandomString.make();

        String headerValue3 = RandomString.make();

        LoggingProperties logProps = LoggingProperties.builder().maskedHeaders(headerName0, headerName1).build();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(headerName0, headerValue0);
        headers.add(headerName1, headerValue1);
        headers.add(headerName2, headerValue2);
        headers.add(headerName0, headerValue3);

        String actual = provider.createMessage(headers, logProps);
        log.info(actual);

        assertAll(
                () -> assertTrue(actual.contains(" HEADERS: [ ")),

                () -> assertFalse(actual.contains(headerName0 + "=" + headerValue0)),
                () -> assertEquals(2, StringUtils.countMatches(actual, headerName0 + "=" + DEFAULT_MASK)),

                () -> assertFalse(actual.contains(headerName1 + "=" + headerValue1)),
                () -> assertTrue(actual.contains(headerName1 + "=" + DEFAULT_MASK)),

                () -> assertFalse(actual.contains(headerName0 + "=" + headerValue3)),
                () -> assertTrue(actual.contains(headerName2 + "=" + headerValue2))
        );
    }
}