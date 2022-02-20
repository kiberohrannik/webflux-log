# webflux-log

**Logging filters for Spring WebFlux client and server request/responses.**

## Usage
**To log WebClient request/response, do the following**

- specify data you want to be logged via `LoggingProperties`
```java
LoggingProperties requestProperties = LoggingProperties.builder()
        .logRequestId(true).requestIdPrefix("TEST")
        .logHeaders(true).maskedHeaders("Authorization")
        .logBody(true)
        .build();

LoggingProperties responseProperties = LoggingProperties.builder()
        .logRequestId(true).requestIdPrefix("TEST")
        .logHeaders(true)
        .logBody(true)
        .build();
```
- create `ClientRequestLoggingFilter` and `ClientResponseLoggingFilter` via factories
```java
ExchangeFilterFunction requestLogFilter = ClientRequestLoggingFilterFactory.defaultFilter(requestProperties);
ExchangeFilterFunction responseLogFilter = ClientResponseLoggingFilterFactory.defaultFilter(responseProperties);
```
- add filters to `WebClient`
```java
WebClient.builder()
        ...
        .filter(requestLogFilter)
        .filter(responseLogFilter)
        .build();
```
- log message example
```java
REQUEST: POST http://localhost:8088/some/test/path REQ-ID: [ TEST_6dd82486 ] HEADERS: [ Accept=application/json Authorization={masked} ] BODY: [ Some request body value ]
RESPONSE: ELAPSED TIME: 233ms STATUS: 200 OK REQ-ID: [ TEST_6dd82486[ad13a534] ] HEADERS: [ Matched-Stub-Id=eccbf9ef-dabc-4659-aec6-1428db585cb7 Vary=Accept-Encoding, User-Agent Transfer-Encoding=chunked Server=Jetty(9.4.44.v20210927) ] BODY: [ response-body 123 ]
```

**To log server request/response, do the following**
- specify data you want to be logged via `LoggingProperties`
```java
LoggingProperties requestProperties = LoggingProperties.builder()
.logRequestId(true)
.logHeaders(true).maskedHeaders("Authorization")
.logBody(true)
.build();

LoggingProperties responseProperties = LoggingProperties.builder()
.logRequestId(true)
.logHeaders(true)
.logCookies(true)
.logBody(true)
.build();
```
- create `LoggingFilter` via `ServerLoggingFilterFactory`
```java
LoggingFilter requestResponseLoggingFilter = ServerLoggingFilterFactory.defaultFilter(requestProperties, responseProperties);
```
- add `LoggingFilter` to WebFilters chain, e. g., registering it as a `WebFilter` bean


- log message example
```java
REQUEST: POST http://localhost:8080/test/endpoint REQ-ID: [ 20206022 ] HEADERS: [ accept-encoding=gzip user-agent=ReactorNetty/1.0.13 host=localhost:8080 content-type=application/json accept=application/json content-length=41 Authorization={masked} ]
REQ-ID: [ 20206022 ] BODY: [ {"value0":"WCh6dSSw","value1":"AI2D7SMs"} ]
        
RESPONSE: ELAPSED TIME: 61ms STATUS: 200 OK REQ-ID: [ 20206022 ] HEADERS: [ Content-Type=application/json;charset=UTF-8 Content-Length=50 ] COOKIES (Set-Cookie): [ ] BODY: [ {"value0":"WCh6dSSw","value1":"AI2D7SMs"}-RESPONSE ]
```