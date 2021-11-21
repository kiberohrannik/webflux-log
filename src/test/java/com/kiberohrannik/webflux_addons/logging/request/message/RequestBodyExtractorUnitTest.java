package com.kiberohrannik.webflux_addons.logging.request.message;

import com.kiberohrannik.webflux_addons.base.BaseTest;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class RequestBodyExtractorUnitTest extends BaseTest {

    @InjectMocks
    private RequestBodyExtractor bodyExtractor;

    @Spy
    private RequestBodyMapper requestBodyMapper;

    private final ClientRequest.Builder reqBuilder = ClientRequest.create(HttpMethod.GET, URI.create("/someUri"));

    @Test
    void extractBody_whenNoBody_thenReturnEmpty() {
        Mono<String> bodyMono = bodyExtractor.extractBody(reqBuilder.build());

        assertDoesNotThrow((ThrowingSupplier<String>) bodyMono::block);
        verifyNoInteractions(requestBodyMapper);
    }

    @Test
    void extractBody_whenWithBody_thenReturnString() {
        String expectedStr = RandomString.make(20);
        ClientRequest requestWithoutBody = reqBuilder.body(Mono.just(expectedStr), String.class).build();

        String actualStr = bodyExtractor.extractBody(requestWithoutBody).block();
        assertEquals(expectedStr, actualStr);
    }
}
