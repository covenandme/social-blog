package com.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.storage")
public class FileStorageProperties {

    /**
     * 本地存储绝对/相对路径
     */
    private String localPath = "uploads";

    /**
     * 对外暴露的访问前缀，例如 /files/
     */
    private String publicPrefix = "/files/";
}

