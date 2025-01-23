package com.basebook.web.config;

import com.basebook.service.config.ServiceConfig;
import com.basebook.web.converter.StringToTagConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.basebook.web")
@Import(ServiceConfig.class)
public class WebConfig implements WebMvcConfigurer {

    public static final String UPLOADS_MAPPING = "/uploads/**";
    public static final String STATIC_MAPPING = "/static/**";
    public static final String IMAGE_UPLOAD_RESOURCE = "${image.upload.resource}";
    public static final String IMAGE_STATIC_RESOURCE = "${image.static.resource}";


    private final StringToTagConverter stringToTagConverter;

    @Value(IMAGE_UPLOAD_RESOURCE)
    private String uploadResource;

    @Value(IMAGE_STATIC_RESOURCE)
    private String staticResource;

    public WebConfig(final StringToTagConverter stringToTagConverter) {
        this.stringToTagConverter = stringToTagConverter;
    }

    @Override
    public void addFormatters(org.springframework.format.FormatterRegistry registry) {
        registry.addConverter(stringToTagConverter); // Регистрация конвертера
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }


    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/posts");
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
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
