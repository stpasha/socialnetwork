package com.basebook.persistence.config;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class YamlPropertySourceFactory implements PropertySourceFactory {


    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        Yaml yaml = new Yaml();
        Map<String, Object> properties = yaml.load(new InputStreamReader(resource.getInputStream()));
        return new MapPropertySource("yamlProperties", properties);
    }
}