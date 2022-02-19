package com.kv.webflux.logging.provider;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

final class ProviderUtils {

    //TODO refactor these methods !!!!


    static <T> MultiValueMap<String, T> setMaskToValues(MultiValueMap<String, T> multiValueMap,
                                                        String[] objectKeysToMask, T mask) {

        MultiValueMap<String, T> copiedMap = new LinkedMultiValueMap<>(multiValueMap);

        for (String maskedName : objectKeysToMask) {
            setMaskToValue(copiedMap, maskedName, mask);
        }

        return copiedMap;
    }

    static <T> void setMaskToValue(MultiValueMap<String, T> sourceMap,
                                   String objectKeyToMask, T mask) {

        List<T> values = sourceMap.get(objectKeyToMask);

        if (values != null && !values.isEmpty()) {
            if (values.size() == 1) {
                sourceMap.put(objectKeyToMask, List.of(mask));

            } else {
                List<T> masked = values.stream()
                        .map(value -> mask)
                        .collect(Collectors.toList());

                sourceMap.put(objectKeyToMask, masked);
            }
        }
    }
}
