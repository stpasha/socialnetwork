package com.basebook.persistence.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Properties;

@ComponentScan("com.basebook.persistence")
@PropertySource("classpath:application.properties")
@Configuration
@Slf4j
public class PersistenceConfig {

    public static final String SCHEMA_SQL = "schema.sql";

    @Bean
    public DataSource dataSource(Environment env, ResourceLoader rl) {
        String[] activeProfiles = env.getActiveProfiles();
        log.debug("Read profile number from properties {}", activeProfiles.length);
        String activeProfile = activeProfiles.length > 0 ? activeProfiles[0] : "test";
        String profileFile = String.format("classpath:application-%s.properties", activeProfile);
        Resource profileResource = rl.getResource(profileFile);
        Properties profileProperties = new Properties();
        try {
            profileProperties.load(profileResource.getInputStream());
            String databaseDriver = profileProperties.getProperty("spring.datasource.driver");
            String url = profileProperties.getProperty("spring.datasource.url");
            String username = profileProperties.getProperty("spring.datasource.username");
            String password = profileProperties.getProperty("spring.datasource.password");
            log.info("Processing db params to DataSource driver {} url {} uname {} paswd {}", databaseDriver, url, username, password);
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(databaseDriver);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            return dataSource;
        } catch (IOException e) {
            throw new IllegalStateException("The error occured due to illegal db profile " + profileFile, e);
        }

    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean // Describes Resource loader
    public ResourceLoader resourceLoader() {
        return new DefaultResourceLoader();
    }

    //Assume dev env db is already preloaded
    @Profile("test")
    @EventListener
    public void populate(ContextRefreshedEvent event) {
        log.info("Start of schema deployment time {}", new Timestamp(event.getTimestamp()).toInstant());
        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource(SCHEMA_SQL)); // Файл должен находится в ресурсах
        populator.execute(dataSource);
        log.info("End of schema deployment time {}", Instant.now());
    }
}
