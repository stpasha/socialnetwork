package com.basebook.persistence.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

//@ComponentScan("com.basebook.persistence")
@Configuration
@Slf4j
public class PersistenceConfig {

    private static final String SCHEMA_SQL = "schema.sql";
    private static final String TESTDATA_SQL = "data.sql";

//    @Bean
//    public DataSource dataSource(final Environment env, ResourceLoader rl) {
//        String[] activeProfiles = env.getActiveProfiles();
//        log.debug("Read profile number from properties {}", activeProfiles.length);
//        String activeProfile = activeProfiles.length > 0 ? activeProfiles[0] : "test";
//        String profileFile = String.format("classpath:application-%s.properties",
//                activeProfile);
//        Resource profileResource = rl.getResource(profileFile);
//        Properties profileProperties = new Properties();
//        try {
//            profileProperties.load(profileResource.getInputStream());
//            String databaseDriver = profileProperties.getProperty(SPRING_DATASOURCE_DRIVER_CLASS_NAME);
//            String url = profileProperties.getProperty(SPRING_DATASOURCE_URL);
//            String username = profileProperties.getProperty(SPRING_DATASOURCE_USERNAME);
//            String password = profileProperties.getProperty(SPRING_DATASOURCE_PASSWORD);
//            log.info(PROCESSING_DB_PARAMS_TO_DATA_SOURCE_DRIVER_URL_UNAME_PASWD,
//                    databaseDriver, url, username, password);
//            DriverManagerDataSource dataSource = new DriverManagerDataSource();
//            dataSource.setDriverClassName(databaseDriver);
//            dataSource.setUrl(url);
//            dataSource.setUsername(username);
//            dataSource.setPassword(password);
//            return dataSource;
//        } catch (IOException e) {
//            throw new IllegalStateException("The error occured due to illegal db profile " + profileFile, e);
//        }
//
//    }

//    @Bean
//    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }

//    @Bean // Describes Resource loader
//    public ResourceLoader resourceLoader() {
//        return new DefaultResourceLoader();
//    }

    //Assume dev env db is already preloaded
//    @Profile("test")
//    @EventListener
//    public void populate(final ContextRefreshedEvent event) {
//        log.info("Start of schema deployment time {}", new Timestamp(event.getTimestamp()).toInstant());
//        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);
//        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//        // Файл должен находится в ресурсах!!!
//        populator.addScripts(new ClassPathResource(SCHEMA_SQL), new ClassPathResource(TESTDATA_SQL));
//        populator.execute(dataSource);
//        log.info("End of schema deployment time {}", Instant.now());
//    }
}
