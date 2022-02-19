package com.kv.webflux.logging.server.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kv.webflux.logging.server.app.TestDto;
import com.kv.webflux.logging.server.base.BaseIntegrationTest;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static com.kv.webflux.logging.server.app.TestController.RESPONSE_PREFIX;
import static com.kv.webflux.logging.server.app.TestController.TEST_ENDPOINT;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoggingFilterComponentTest extends BaseIntegrationTest {

    @Test
    void filter_whenReturned404_thenLogAllRequestExceptBody_andFullResponse(CapturedOutput output) {
        String randomUri = "notExistingEndpoint";
        Mono<?> responseMono = createWebClient().post()
                .uri(randomUri)
                .retrieve()
                .onStatus(status -> status.equals(NOT_FOUND), response -> Mono.error(new MockException()))
                .toBodilessEntity();

        assertThrows(MockException.class, responseMono::block);

        String logs = output.getAll();
        assertTrue(logs.contains("REQUEST: POST http://localhost:8080/" + randomUri));
        assertEquals(2, StringUtils.countMatches(logs, " REQ-ID: [ TEST-REQ-ID_"));
        assertTrue(logs.contains(" HEADERS: [ "));
        assertTrue(logs.contains(" COOKIES: [ ]"));

        assertTrue(logs.contains(" RESPONSE: ELAPSED TIME: "));
        assertTrue(logs.contains(" STATUS: 404 Not Found"));
        assertTrue(logs.contains(" HEADERS: [ Content-Type=application/json"));
        assertTrue(logs.contains(" COOKIES (Set-Cookie): [ ] BODY: [ {no body} ]"));

        assertEquals(1, StringUtils.countMatches(logs, "BODY"));
    }

    @Test
    void filter_whenReturned400_thenLogFullRequest_andFullResponse(CapturedOutput output) {
        String invalidRequestBody = RandomString.make();

        Mono<?> responseMono = createWebClient().post()
                .uri(TEST_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequestBody)
                .retrieve()
                .onStatus(status -> status.equals(BAD_REQUEST), response -> Mono.error(new MockException()))
                .toBodilessEntity();

        assertThrows(MockException.class, responseMono::block);

        String logs = output.getAll();
        assertTrue(logs.contains("REQUEST: POST http://localhost:8080" + TEST_ENDPOINT));
        assertEquals(3, StringUtils.countMatches(logs, " REQ-ID: [ TEST-REQ-ID_"));
        assertTrue(logs.contains(" HEADERS: [ "));
        assertTrue(logs.contains(" COOKIES: [ ]"));
        assertTrue(logs.contains(" BODY: [ " + invalidRequestBody + " ]"));

        assertTrue(logs.contains(" RESPONSE: ELAPSED TIME: "));
        assertTrue(logs.contains(" STATUS: 400 Bad Request"));
        assertTrue(logs.contains(" HEADERS: [ Content-Type=application/json"));
        assertTrue(logs.contains(" COOKIES (Set-Cookie): [ ] BODY: [ {no body} ]"));
    }

    @Test
    void filter_whenReturned200_thenLogFullRequest_andFullResponse(CapturedOutput output)
            throws JsonProcessingException {

        TestDto requestBody = new TestDto(RandomString.make(), RandomString.make());
        String validReqJson = new ObjectMapper().writeValueAsString(requestBody);

        ResponseEntity<String> response = createWebClient().post()
                .uri(TEST_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(requestBody))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String expectedResponseBody = validReqJson + RESPONSE_PREFIX;
        assertEquals(expectedResponseBody, response.getBody());

        String logs = output.getAll();
        assertTrue(logs.contains("REQUEST: POST http://localhost:8080" + TEST_ENDPOINT));
        assertEquals(3, StringUtils.countMatches(logs, " REQ-ID: [ TEST-REQ-ID_"));
        assertTrue(logs.contains(" HEADERS: [ "));
        assertTrue(logs.contains(" COOKIES: [ ]"));
        assertTrue(logs.contains(" BODY: [ " + validReqJson + " ]"));

        assertTrue(logs.contains(" RESPONSE: ELAPSED TIME: "));
        assertTrue(logs.contains(" STATUS: 200 OK"));
        assertTrue(logs.contains(" HEADERS: [ Content-Type=application/json"));
        assertTrue(logs.contains(" COOKIES (Set-Cookie): [ ] BODY: [ " + expectedResponseBody + " ]"));
    }


    public static class MockException extends RuntimeException {
    }
}
