package com.basebook.test.persistence.config;


import com.basebook.util.TestDataFactory;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Configuration
@ComponentScan("com.basebook.test.persistence")
@ComponentScan("com.basebook.persistence.repository")
@PropertySource(value = "classpath:test-persistence.properties")
@ActiveProfiles("test")
public class TestRepositoryConfig {

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    @Value("${database.driver}")
    private String driver;

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean // Describes Resource loader
    public ResourceLoader resourceLoader() {
        return new DefaultResourceLoader();
    }

    private static final String SCHEMA_SQL = "schema.sql";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    //Assume dev env db is already preloaded
    @Profile("test")
    @EventListener
    public void populate(final ContextRefreshedEvent event) {
        log.info("Start of schema deployment time {}", new Timestamp(event.getTimestamp()).toInstant());
        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource(SCHEMA_SQL));
        populator.execute(dataSource);
        log.info("End of schema deployment time {}", Instant.now());
    }


    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public TestDataFactory testDataFactory() {
        return new TestDataFactory(faker());
    }
}
