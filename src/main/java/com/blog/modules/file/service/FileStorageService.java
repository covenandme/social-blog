package com.blog.modules.file.service;

import com.blog.config.FileStorageProperties;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final FileStorageProperties properties;
    private final Path rootPath;

    public FileStorageService(FileStorageProperties properties) {
        this.properties = properties;
        this.rootPath = Paths.get(properties.getLocalPath()).toAbsolutePath();
    }

    public List<String> store(List<MultipartFile> files) {
        ensureDirectory();
        return files.stream()
                .filter(file -> !file.isEmpty())
                .map(this::saveFile)
                .collect(Collectors.toList());
    }

    private String saveFile(MultipartFile file) {
        try {
            String original = file.getOriginalFilename();
            String ext = "";
            if (StringUtils.hasText(original) && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.'));
            }
            String filename = UUID.randomUUID() + ext;
            Path target = rootPath.resolve(filename);
            file.transferTo(target);
            String prefix = properties.getPublicPrefix();
            if (!prefix.endsWith("/")) {
                prefix = prefix + "/";
            }
            if (!prefix.startsWith("/")) {
                prefix = "/" + prefix;
            }
            return prefix + filename;
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败", e);
        }
    }

    private void ensureDirectory() {
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new RuntimeException("创建上传目录失败", e);
        }
    }
}

