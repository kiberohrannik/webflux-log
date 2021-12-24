package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqIdProviderUnitTest extends BaseTest {

    private final ReqIdProvider provider = new ReqIdProvider();


    @Test
    void createMessage_whenReqIdNoPrefix_thenAddJustReqId() {
        String reqId = RandomString.make();
        String logPrefix = "[ " + reqId + " ]";

        LoggingProperties logProps = LoggingProperties.builder().logRequestId(true).build();

        String actual = provider.createFromLogPrefix(logPrefix, logProps, "");
        log.info(actual);

        assertEquals(actual, " REQ-ID: [ " + reqId + " ]");
    }

    @Test
    void createMessage_whenClientResponseReqIds_thenAddAllReqIds() {
        String reqId0 = RandomString.make();
        String reqId1 = RandomString.make();
        String logPrefix = "[ " + reqId0 + " ]" + "[ " + reqId1 + " ]";

        LoggingProperties logProps = LoggingProperties.builder().logRequestId(true).build();

        String actual = provider.createFromLogPrefix(logPrefix, logProps, "");
        log.info(actual);

        assertEquals(actual, " REQ-ID: [ " + reqId0 + "[" + reqId1 + "] ]");
    }

    @Test
    void createMessage_whenReqIdWithPrefix_thenAddWithPrefix() {
        String reqId = RandomString.make();
        String logPrefix = "[ " + reqId + " ]";
        String reqIdPrefix = RandomString.make();

        LoggingProperties logProps = LoggingProperties.builder()
                .logRequestId(true)
                .requestIdPrefix(reqIdPrefix)
                .build();

        String actual = provider.createFromLogPrefix(logPrefix, logProps, "");
        log.info(actual);

        assertEquals(actual, " REQ-ID: [ " + reqIdPrefix + "_" + reqId + " ]");
    }
}