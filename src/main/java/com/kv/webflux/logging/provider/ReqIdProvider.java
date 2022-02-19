package com.kv.webflux.logging.provider;

import com.kv.webflux.logging.client.LoggingProperties;
import com.kv.webflux.logging.client.LoggingUtils;

public final class ReqIdProvider {

    public String createFromLogPrefix(String logPrefix, LoggingProperties properties) {
        return properties.isLogRequestId()
                ? create(formatReqId(logPrefix), properties.getRequestIdPrefix())
                : LoggingUtils.EMPTY_MESSAGE;
    }


    public String createFromLogId(String logId, LoggingProperties properties) {
        return properties.isLogRequestId()
                ? create(logId, properties.getRequestIdPrefix()).trim()
                : LoggingUtils.EMPTY_MESSAGE;
    }


    private String create(String reqId, String reqIdPrefix) {
        if (reqIdPrefix != null) {
            reqId = reqIdPrefix.concat("_").concat(reqId);
        }

        return " REQ-ID: [ ".concat(reqId).concat(" ]");
    }

    private String formatReqId(String logPrefix) {
        return logPrefix.replaceFirst("\\[", "").replaceFirst("]", "").replaceAll("\\s", "");
    }
}
