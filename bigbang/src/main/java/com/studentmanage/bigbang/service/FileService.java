package com.studentmanage.bigbang.service;

import com.studentmanage.bigbang.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传服务
 * 存储到本地 ./uploads 目录，通过 /uploads/** 映射访问
 */
@Service
public class FileService {

    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    /** 上传文件，返回访问 URL */
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("文件为空");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null) originalName = "unknown";
        String extension = originalName.contains(".") ?
                originalName.substring(originalName.lastIndexOf(".")) : "";
        String newName = UUID.randomUUID() + extension;

        try {
            Path dir = Paths.get(uploadDir);
            if (!Files.exists(dir)) Files.createDirectories(dir);
            Path target = dir.resolve(newName);
            Files.write(target, file.getBytes());
            return "/uploads/" + newName;
        } catch (IOException e) {
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }
}
