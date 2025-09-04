package com.example.controllbuilding.controller;

import com.example.controllbuilding.model.DTO.FileContent;
import com.example.controllbuilding.service.FileContentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileContentController {

    private final FileContentService fileContentService;

    // Загрузка одного файла
    @PostMapping("/upload")
    public ResponseEntity<FileContent> upload(
            @RequestParam MultipartFile file,
            @RequestParam String type,
            @RequestParam Long objectId) throws IOException {

        FileContent metadata = fileContentService.uploadFile(file, type, objectId);
        return ResponseEntity.ok(metadata);
    }

    // Загрузка нескольких файлов
    @PostMapping("/upload-multiple")
    public ResponseEntity<List<FileContent>> uploadMultiple(
            @RequestParam List<MultipartFile> files,
            @RequestParam String type,
            @RequestParam Long objectId) throws IOException {

        List<FileContent> uploadedFiles = fileContentService.uploadFiles(files, type, objectId);
        return ResponseEntity.ok(uploadedFiles);
    }

    // Скачивание одного файла
    @GetMapping("/download/{id}")
    public void download(
            @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        fileContentService.downloadFile(id, response);
    }

    // Просмотр одного файла (inline)
    @GetMapping("/view/{id}")
    public void view(
            @PathVariable Long id,
            HttpServletResponse response) throws IOException {
        fileContentService.viewFile(id, response);
    }

    // Просмотр нескольких файлов
    @GetMapping("/view-multiple")
    public void viewMultiple(
            @RequestParam String type,
            @RequestParam Long objectId,
            HttpServletResponse response) throws IOException {

        fileContentService.viewMultipleFiles(type, objectId, response);
    }

    // Получение всех метаданных файлов
    @GetMapping("/list")
    public ResponseEntity<List<FileContent>> listFilesMetadata( ) {
        List<FileContent> files = fileContentService.getAllMetadataFiles();
        return ResponseEntity.ok(files);
    }

    //Получение списка всех файлов по id
    @GetMapping("/list/{id}")
    public ResponseEntity<List<FileContent>> listFilesMetadataById(
            @RequestParam String type,
            @PathVariable("id") Long objectId){
        List<FileContent> files = fileContentService.getFilesMetadataById(type,objectId);
        return ResponseEntity.ok(files);
    }



    // Просмотр метаданных файла
    @GetMapping("/{id}")
    public ResponseEntity<FileContent> getFileMetadata(@PathVariable Long id) {
        return ResponseEntity.ok(fileContentService.getMetadataById(id));
    }

    // Удаление файла (логическое)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileContentService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }


    // Обновление метаданных файла (например, названия, описания)
    @PutMapping("/{id}")
    public ResponseEntity<FileContent> updateMetadata(
            @PathVariable Long id,
            @RequestBody FileContent fileContent) {
        FileContent updated = fileContentService.updateMetadata(id, fileContent);
        return ResponseEntity.ok(updated);
    }



}