package com.kiberohrannik.webflux_addons.logging.client.request.message.formatter.extractor;

import com.kiberohrannik.webflux_addons.base.BaseTest;
import lombok.Data;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestBodyMapperUnitTest extends BaseTest {

    private static final String BODY_CONTENT_STR = RandomString.make(40);
    private static final TestDto BODY_CONTENT_OBJ = generateTestDto();

    private final RequestBodyMapper bodyMapper = new RequestBodyMapper();


    @ParameterizedTest
    @MethodSource("getStringBodyValuesAndTypes")
    void mapToString_whenStringBody_thenReturnBodyString(Object bodyValue, Object bodyType) {
        Mono<String> result = bodyMapper.mapToString(bodyValue, bodyType);
        assertEquals(BODY_CONTENT_STR, result.block());
    }

    @ParameterizedTest
    @MethodSource("getObjectBodyValuesAndTypes")
    void mapToString_whenObjectBody_thenReturnBodyString(Object bodyValue, Object bodyType) {
        Mono<String> result = bodyMapper.mapToString(bodyValue, bodyType);
        assertEquals(BODY_CONTENT_OBJ.toString(), result.block());
    }

    @Test
    void mapToString_whenBodyExistsButNotSupportedType_thenReturnEmpty() {
        Mono<String> result = bodyMapper.mapToString(new byte[]{2, 2, 8}, null);
        assertNull(result.block());
    }


    private static Stream<Arguments> getStringBodyValuesAndTypes() {
        return Stream.of(
                Arguments.of(BODY_CONTENT_STR, null),
                Arguments.of(Mono.just(BODY_CONTENT_STR), null),
                Arguments.of(Mono.defer(() -> Mono.just(BODY_CONTENT_STR)), null)
        );
    }

    private static Stream<Arguments> getObjectBodyValuesAndTypes() {
        return Stream.of(
                Arguments.of(BODY_CONTENT_OBJ, TestDto.class),
                Arguments.of(BODY_CONTENT_OBJ, new ParameterizedTypeReference<TestDto>() {
                }),

                Arguments.of(Mono.just(BODY_CONTENT_OBJ), TestDto.class),
                Arguments.of(Mono.just(BODY_CONTENT_OBJ), new ParameterizedTypeReference<TestDto>() {
                })
        );
    }


    private static TestDto generateTestDto() {
        TestDto testDto = new TestDto();
        testDto.value0 = RandomString.make();
        testDto.value1 = RandomString.make();
        return testDto;
    }


    @Data
    static class TestDto {
        private String value0;
        private String value1;
    }
}
