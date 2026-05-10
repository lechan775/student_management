package com.studentmanage.bigbang.controller;

import com.studentmanage.bigbang.model.dto.ApiResponse;
import com.studentmanage.bigbang.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Tag(name = "文件管理", description = "头像/附件上传")
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public ApiResponse<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        String url = fileService.uploadFile(file);
        return ApiResponse.success(Map.of("url", url));
    }
}
