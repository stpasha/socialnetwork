package com.basebook.test.web.config;

import com.basebook.util.TestDataFactory;
import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.basebook.test.web"})
public class TestWebConfig {
    @Bean
    public Faker faker() {
        return new Faker();
    }
}
