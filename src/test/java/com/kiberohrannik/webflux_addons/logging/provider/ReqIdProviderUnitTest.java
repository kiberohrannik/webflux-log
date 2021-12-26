package com.kiberohrannik.webflux_addons.logging.provider;

import com.kiberohrannik.webflux_addons.logging.base.BaseTest;
import com.kiberohrannik.webflux_addons.logging.client.LoggingProperties;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqIdProviderUnitTest extends BaseTest {

    private final ReqIdProvider provider = new ReqIdProvider();

    private final String sourceMessage = "some base message ...";

    private final LoggingProperties propsDontLogReqId = LoggingProperties.builder().build();
    private final LoggingProperties propsLogReqId = LoggingProperties.builder().logRequestId(true).build();

    private final LoggingProperties propsLogReqIdWithPrefix = LoggingProperties.builder()
            .logRequestId(true)
            .requestIdPrefix(RandomString.make(10))
            .build();


    @Test
    void createFromLogPrefix_whenDontLogReqId_thenReturnSourceMessage() {
        String actual = provider.createFromLogPrefix(RandomString.make(), propsDontLogReqId, sourceMessage);
        log.info(actual);

        assertEquals(sourceMessage, actual);
    }

    @Test
    void createFromLogPrefix_whenReqIdNoPrefix_thenAddJustReqId() {
        String reqId = RandomString.make();
        String logPrefix = "[ " + reqId + " ]";

        String actual = provider.createFromLogPrefix(logPrefix, propsLogReqId, sourceMessage);
        log.info(actual);

        assertEquals(sourceMessage + " REQ-ID: [ " + reqId + " ]", actual);
    }

    @Test
    void createFromLogPrefix_whenClientResponseReqIds_thenAddAllReqIds() {
        String reqId0 = RandomString.make();
        String reqId1 = RandomString.make();
        String logPrefix = "[ " + reqId0 + " ]" + "[ " + reqId1 + " ]";

        String actual = provider.createFromLogPrefix(logPrefix, propsLogReqId, sourceMessage);
        log.info(actual);

        assertEquals(sourceMessage + " REQ-ID: [ " + reqId0 + "[" + reqId1 + "] ]", actual);
    }

    @Test
    void createFromLogPrefix_whenReqIdWithPrefix_thenAddWithPrefix() {
        String reqId = RandomString.make();
        String logPrefix = "[ " + reqId + " ]";

        String actual = provider.createFromLogPrefix(logPrefix, propsLogReqIdWithPrefix, sourceMessage);
        log.info(actual);

        assertEquals(sourceMessage + " REQ-ID: [ " + propsLogReqIdWithPrefix.getRequestIdPrefix() + "_" + reqId + " ]",
                actual);
    }


    @Test
    void createFromLogId_whenDontLogReqId_thenReturnEmptyStr() {
        String actual = provider.createFromLogId(RandomString.make(), propsDontLogReqId);
        log.info(actual);

        assertEquals("", actual);
    }

    @Test
    void createFromLogId_whenReqIdNoPrefix_thenAddJustLogId() {
        String logId = RandomString.make();

        String actual = provider.createFromLogId(logId, propsLogReqId);
        log.info(actual);

        assertEquals("REQ-ID: [ " + logId + " ]", actual);
    }

    @Test
    void createFromLogId_whenReqIdWithPrefix_thenAddWithPrefix() {
        String logId = RandomString.make();

        String actual = provider.createFromLogId(logId, propsLogReqIdWithPrefix);
        log.info(actual);

        assertEquals("REQ-ID: [ " + propsLogReqIdWithPrefix.getRequestIdPrefix() + "_" + logId + " ]", actual);
    }
}