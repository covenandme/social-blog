package com.blog.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StorageConfig implements WebMvcConfigurer {

    private final FileStorageProperties properties;

    public StorageConfig(FileStorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String prefix = properties.getPublicPrefix();
        if (!StringUtils.hasText(prefix)) {
            prefix = "/files/";
        }
        if (!prefix.endsWith("/")) {
            prefix = prefix + "/";
        }
        Path uploadPath = Paths.get(properties.getLocalPath()).toAbsolutePath();
        registry.addResourceHandler(prefix + "**")
                .addResourceLocations("file:" + uploadPath.toString() + "/");
    }
}

