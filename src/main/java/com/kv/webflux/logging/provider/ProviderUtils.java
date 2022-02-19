package com.kv.webflux.logging.provider;

import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class ProviderUtils {

    static <T> Map<String, List<T>> setMaskToValues(Map<String, List<T>> multiValueMap,
                                                    String[] objectKeysToMask, T mask) {

        LinkedCaseInsensitiveMap<List<T>> caseInsensitiveMap = new LinkedCaseInsensitiveMap<>();
        caseInsensitiveMap.putAll(multiValueMap);

        for (String maskedName : objectKeysToMask) {
            setMaskToValue(caseInsensitiveMap, maskedName, mask);
        }

        return caseInsensitiveMap;
    }

    static <T> void setMaskToValue(LinkedCaseInsensitiveMap<List<T>> caseInsensitiveMap,
                                   String objectKeyToMask, T mask) {

        List<T> values = caseInsensitiveMap.get(objectKeyToMask);

        if (values != null && !values.isEmpty()) {
            if (values.size() == 1) {
                caseInsensitiveMap.put(objectKeyToMask, List.of(mask));

            } else {
                List<T> masked = values.stream()
                        .map(value -> mask)
                        .collect(Collectors.toList());

                caseInsensitiveMap.put(objectKeyToMask, masked);
            }
        }
    }
}
