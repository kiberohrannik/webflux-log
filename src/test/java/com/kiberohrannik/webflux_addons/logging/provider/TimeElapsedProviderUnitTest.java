package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

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

    @Test
    void createMessage_whenMoreThan999Millis_thenAddWithS() {
        long timeElapsedMillis = new Random().nextInt(99999) + 1000;

        String actual = provider.createMessage(timeElapsedMillis);
        log.info(actual);

        assertEquals(" ELAPSED TIME: " + TimeUnit.MILLISECONDS.toSeconds(timeElapsedMillis) + "s", actual);
    }
}