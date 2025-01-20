package com.basebook.web.config;

import com.basebook.service.TagService;
import com.basebook.service.config.ServiceConfig;
import com.basebook.web.converter.StringToTagConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.basebook.web")
@Import(ServiceConfig.class)
public class WebConfig implements WebMvcConfigurer {

    public static final String UPLOADS_MAPPING = "/uploads/**";
    public static final String STATIC_MAPPING = "/static/**";


    private final StringToTagConverter stringToTagConverter;

    @Value("${image.upload.resource}")
    private String uploadResource;

    @Value("${image.static.resource}")
    private String staticResource;

    public WebConfig(StringToTagConverter stringToTagConverter) {
        this.stringToTagConverter = stringToTagConverter;
    }

    @Override
    public void addFormatters(org.springframework.format.FormatterRegistry registry) {
        registry.addConverter(stringToTagConverter); // Регистрация конвертера
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/posts");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(STATIC_MAPPING)
                .addResourceLocations(staticResource);
        registry.addResourceHandler(UPLOADS_MAPPING)
                .addResourceLocations(uploadResource);
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
