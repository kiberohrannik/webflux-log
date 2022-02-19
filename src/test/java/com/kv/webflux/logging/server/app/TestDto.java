package com.kv.webflux.logging.server.app;

import java.io.Serializable;
import java.util.Objects;


public class TestDto implements Serializable {
    private String value0;
    private String value1;

    public TestDto() {
    }

    public TestDto(String value0, String value1) {
        this.value0 = value0;
        this.value1 = value1;
    }

    public String getValue0() {
        return value0;
    }

    public void setValue0(String value0) {
        this.value0 = value0;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestDto testDto = (TestDto) o;
        return Objects.equals(value0, testDto.value0) && Objects.equals(value1, testDto.value1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value0, value1);
    }

    @Override
    public String toString() {
        return "TestDto{" +
                "value0='" + value0 + '\'' +
                ", value1='" + value1 + '\'' +
                '}';
    }
}