package com.kiberohrannik.webflux_addons.logging.client;

public final class LoggingProperties {

    private final boolean logRequestId;
    private final String requestIdPrefix;

    private final boolean logHeaders;
    private final String[] maskedHeaders;

    private final boolean logCookies;
    private final String[] maskedCookies;

    private final boolean logBody;


    private LoggingProperties(final boolean logHeaders, final String[] maskedHeaders,
                              final boolean logCookies, final String[] maskedCookies,
                              final boolean logBody,
                              final boolean logRequestId, final String requestIdPrefix) {

        this.logHeaders = logHeaders;
        this.maskedHeaders = maskedHeaders;
        this.logCookies = logCookies;
        this.maskedCookies = maskedCookies;
        this.logBody = logBody;
        this.logRequestId = logRequestId;
        this.requestIdPrefix = requestIdPrefix;
    }


    public static LoggingProperties.LoggingPropertiesBuilder builder() {
        return new LoggingProperties.LoggingPropertiesBuilder();
    }


    public boolean isLogRequestId() {
        return this.logRequestId;
    }

    public String getRequestIdPrefix() {
        return this.requestIdPrefix;
    }

    public boolean isLogHeaders() {
        return this.logHeaders;
    }

    public String[] getMaskedHeaders() {
        return this.maskedHeaders;
    }

    public boolean isLogCookies() {
        return this.logCookies;
    }

    public String[] getMaskedCookies() {
        return this.maskedCookies;
    }

    public boolean isLogBody() {
        return this.logBody;
    }


    public static class LoggingPropertiesBuilder {

        private boolean logRequestId;
        private String requestIdPrefix;

        private boolean logHeaders;
        private String[] maskedHeaders;

        private boolean logCookies;
        private String[] maskedCookies;

        private boolean logBody;


        private LoggingPropertiesBuilder() {
        }


        public LoggingProperties.LoggingPropertiesBuilder logHeaders(final boolean logHeaders) {
            this.logHeaders = logHeaders;
            return this;
        }

        public LoggingProperties.LoggingPropertiesBuilder maskedHeaders(final String... maskedHeaders) {
            this.maskedHeaders = maskedHeaders;
            return this;
        }

        public LoggingProperties.LoggingPropertiesBuilder logCookies(final boolean logCookies) {
            this.logCookies = logCookies;
            return this;
        }

        public LoggingProperties.LoggingPropertiesBuilder maskedCookies(final String... maskedCookies) {
            this.maskedCookies = maskedCookies;
            return this;
        }

        public LoggingProperties.LoggingPropertiesBuilder logBody(final boolean logBody) {
            this.logBody = logBody;
            return this;
        }

        public LoggingProperties.LoggingPropertiesBuilder logRequestId(final boolean logRequestId) {
            this.logRequestId = logRequestId;
            return this;
        }

        public LoggingProperties.LoggingPropertiesBuilder requestIdPrefix(final String requestIdPrefix) {
            this.requestIdPrefix = requestIdPrefix;
            return this;
        }

        public LoggingProperties build() {
            return new LoggingProperties(this.logHeaders, this.maskedHeaders, this.logCookies, this.maskedCookies,
                    this.logBody, this.logRequestId, this.requestIdPrefix);
        }
    }
}