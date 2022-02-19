package com.kv.webflux.logging.provider;

import com.kv.webflux.logging.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeElapsedProviderUnitTest extends BaseTest {

    private final TimeElapsedProvider provider = new TimeElapsedProvider();


    @Test
    void createMessage_whenLessThan1000Millis_thenAddWithMS() {
        long timeElapsedMillis = new Random().nextInt(1000);

        String actual = provider.createMessage(timeElapsedMillis);
        log.info(actual);

        assertEquals(" ELAPSED TIME: " + timeElapsedMillis + "ms", actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1234, 2300, 2004, 6056})
    void createMessage_whenMoreThan999Millis_thenAddWithS(long timeElapsedMillis) {

        String actual = provider.createMessage(timeElapsedMillis);
        log.info(actual);

        String millisStr = String.valueOf(timeElapsedMillis);
        String result = millisStr.substring(0, 1).concat(".").concat(millisStr.substring(1));

        assertEquals(" ELAPSED TIME: " + result + "s", actual);
    }
}