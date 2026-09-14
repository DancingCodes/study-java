package com.study.filedemo.controller;

import com.study.filedemo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${app.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.error(400, "请选择文件");
        }

        String originalName = file.getOriginalFilename();
        log.info("收到文件上传请求，原始文件名：{}，大小：{} bytes", originalName, file.getSize());

        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String newName = UUID.randomUUID().toString() + extension;

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest = new File(dir, newName);
        file.transferTo(dest);

        log.info("文件保存成功，新文件名：{}", newName);
        return Result.success(newName);
    }

    @PostMapping("/upload-multi")
    public Result<List<String>> uploadMulti(@RequestParam("files") MultipartFile[] files) throws IOException {
        if (files.length == 0) {
            return Result.error(400, "请选择文件");
        }

        log.info("收到批量上传请求，文件数量：{}", files.length);

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<String> fileNames = new java.util.ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            String newName = UUID.randomUUID().toString() + extension;

            File dest = new File(dir, newName);
            file.transferTo(dest);
            fileNames.add(newName);
            log.info("文件保存成功：{} -> {}", originalName, newName);
        }

        return Result.success(fileNames);
    }

    @GetMapping
    public Result<List<String>> listFiles() {
        log.info("收到请求：查询文件列表");
        File dir = new File(uploadDir);
        String[] fileNames = dir.list();
        if (fileNames == null) {
            return Result.success(List.of());
        }
        return Result.success(List.of(fileNames));
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename) throws IOException {
        log.info("收到文件下载请求，文件名：{}", filename);

        File file = new File(uploadDir, filename);
        if (!file.exists()) {
            log.warn("文件不存在：{}", filename);
            throw new RuntimeException("文件不存在：" + filename);
        }

        Resource resource = new UrlResource(file.toURI());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @DeleteMapping("/{filename}")
    public Result<Void> delete(@PathVariable String filename) {
        log.info("收到文件删除请求，文件名：{}", filename);

        File file = new File(uploadDir, filename);
        if (!file.exists()) {
            log.warn("文件不存在：{}", filename);
            return Result.error(404, "文件不存在");
        }

        boolean deleted = file.delete();
        if (deleted) {
            log.info("文件删除成功：{}", filename);
            return Result.success();
        } else {
            log.error("文件删除失败：{}", filename);
            return Result.error(500, "文件删除失败");
        }
    }
}
