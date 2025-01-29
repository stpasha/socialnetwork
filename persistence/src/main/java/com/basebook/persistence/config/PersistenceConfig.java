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
import org.yaml.snakeyaml.Yaml;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

@ComponentScan("com.basebook.persistence")
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
@Configuration
@Slf4j
public class PersistenceConfig {

    private static final String SCHEMA_SQL = "schema.sql";
    private static final String TESTDATA_SQL = "testdata.sql";
    public static final String PROCESSING_DB_PARAMS_TO_DATA_SOURCE_DRIVER_URL_UNAME_PASWD =
            "Processing db params to DataSource driver {} url {} uname {} paswd {}";

    @Bean
    public DataSource dataSource(final Environment env, ResourceLoader rl) {
        String[] activeProfiles = env.getActiveProfiles();
        log.debug("Read profile number from properties {}", activeProfiles.length);
        String activeProfile = activeProfiles.length > 0 ? activeProfiles[0] : "test";
        String profileFile = String.format("classpath:application-%s.yaml",
                activeProfile);
        Resource profileResource = rl.getResource(profileFile);
        Yaml profileProperties = new Yaml();
        try {
            Map<String, Object> properties = profileProperties.load(profileResource.getInputStream());
            Map<String, Object> spring = (Map<String, Object>) properties.get("spring");
            Map<String, Object> datasource = (Map<String, Object>) spring.get("datasource");
            String databaseDriver = (String) datasource.get("driver-class-name");
            String url = (String) datasource.get("url");
            String username = (String) datasource.get("username");
            String password = (String) datasource.get("password");

            log.info(PROCESSING_DB_PARAMS_TO_DATA_SOURCE_DRIVER_URL_UNAME_PASWD,
                    databaseDriver, url, username, password);
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
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean // Describes Resource loader
    public ResourceLoader resourceLoader() {
        return new DefaultResourceLoader();
    }

    //Assume dev env db is already preloaded
    @Profile("test")
    @EventListener
    public void populate(final ContextRefreshedEvent event) {
        log.info("Start of schema deployment time {}", new Timestamp(event.getTimestamp()).toInstant());
        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        // Файл должен находится в ресурсах!!!
        populator.addScripts(new ClassPathResource(SCHEMA_SQL), new ClassPathResource(TESTDATA_SQL));
        populator.execute(dataSource);
        log.info("End of schema deployment time {}", Instant.now());
    }
}
