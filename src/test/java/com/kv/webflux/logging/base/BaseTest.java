package com.kv.webflux.logging.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ActiveProfiles("test")
public abstract class BaseTest {

    protected static Log log;


    @BeforeAll
    static void setUpLogger(TestInfo testInfo) {
        Optional<Class<?>> testClass = testInfo.getTestClass();
        assumeTrue(testClass.isPresent());

        log = LogFactory.getLog(testClass.get());
    }
}
