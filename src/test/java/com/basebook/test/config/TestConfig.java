package com.basebook.test.config;

import com.basebook.util.TestDataFactory;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public Faker faker() {
        return new Faker();
    }


    @Bean
    public TestDataFactory testDataFactory(Faker faker) {
        return new TestDataFactory(faker);
    }
}
