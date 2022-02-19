package com.kv.webflux.logging.provider;

public class TimeElapsedProvider {

    public String createMessage(long timeElapsedMillis) {
        String responseTime = timeElapsedMillis < 1000
                ? timeElapsedMillis + "ms"
                : toSeconds(timeElapsedMillis) + "s";

        return " ELAPSED TIME: " + responseTime;
    }

    private String toSeconds(long millis) {
        String millisStr = String.valueOf(millis);
        int millisStartIndex = millisStr.length() - 3;

        String secPart = millisStr.substring(0, millisStartIndex);
        String millisPart = millisStr.substring(millisStartIndex);
        return secPart.concat(".").concat(millisPart);
    }
}