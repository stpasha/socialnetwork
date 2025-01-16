package com.basebook.service.config;

import com.basebook.persistence.config.PersistenceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PersistenceConfig.class)
@ComponentScan(basePackages = "com.basebook.service") // Укажите пакет с вашими сервисами
public class ServiceConfig {
}