package com.kiberohrannik.webflux_addons.logging.request.message;

import com.kiberohrannik.webflux_addons.base.BaseTest;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestBodyMapperUnitTest extends BaseTest {

    private static final String BODY_CONTENT = RandomString.make(40);

    private final RequestBodyMapper bodyMapper = new RequestBodyMapper();


    @ParameterizedTest
    @MethodSource("getBodyValuesAndTypes")
    void mapToString_whenBodyExists_thenReturnBodyString(Object bodyValue, Object bodyType) {
        Mono<String> result = bodyMapper.mapToString(bodyValue, bodyType);
        assertEquals(BODY_CONTENT, result.block());
    }


    private static Stream<Arguments> getBodyValuesAndTypes() {
        return Stream.of(
                Arguments.of(BODY_CONTENT, null),
                Arguments.of(Mono.just(BODY_CONTENT), null),
                Arguments.of(Mono.defer(() -> Mono.just(BODY_CONTENT)), null),
                Arguments.of(Mono.just(BODY_CONTENT), String.class),
                Arguments.of(Mono.just(BODY_CONTENT), new ParameterizedTypeReference<String>() {
                })
        );
    }
}
