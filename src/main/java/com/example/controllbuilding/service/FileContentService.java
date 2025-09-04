package com.example.controllbuilding.service;

import com.example.controllbuilding.mapper.FileContentMapper;
import com.example.controllbuilding.model.DTO.FileContent;
import com.example.controllbuilding.model.entity.FileContentEntity;
import com.example.controllbuilding.repository.FileContentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileContentService {
    private final FileContentRepository fileContentRepository;
    private final FTPService ftpService;


    //Загрузка файла на FTP-сервер и сохранение метаданных
    public FileContent uploadFile(
            MultipartFile file,
            String type,
            Long objectId) throws IOException {
        log.info("Получен файл с фронта: originalName='{}', contentType='{}', size={} байт, type='{}', objectId={}",
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                type,
                objectId);


        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.') + 1);
        }

        String uniqueFileName = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);

        String ftpFilePath = type.toLowerCase() + "/" + uniqueFileName;

        String mimeType = file.getContentType();
        if (mimeType == null || mimeType.isEmpty()) {
            // Попытка определить mimeType по расширению файла
            mimeType = URLConnection.guessContentTypeFromName(originalName);
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // универсальный бинарный тип
            }
        }
        ftpService.uploadFile(ftpFilePath, file.getInputStream());

        FileContent metadata = new FileContent();
        metadata.setName(file.getOriginalFilename());
        metadata.setFtpPath(ftpFilePath);
        metadata.setType(type.toLowerCase());
        metadata.setDeleted(false);
        metadata.setObjectId(objectId);
        metadata.setMimeType(mimeType);
        if (isReport(type)){
            metadata.setReportId(objectId);
        }else metadata.setBuildingId(objectId);
        return FileContentMapper.toDto(fileContentRepository.save(FileContentMapper.toEntity(metadata)));
    }

    // Загрузка нескольких файлов
    public List<FileContent> uploadFiles(
            List<MultipartFile> files,
            String type,
            Long objectId) throws IOException {
        log.info("Получена загрузка нескольких файлов: количество={}, type='{}', objectId={}",
                files.size(),
                type,
                objectId);
        return files.stream()
                .map(file -> {
                    try {
                        return uploadFile(file, type, objectId);
                    } catch (IOException e) {
                        throw new RuntimeException("Ошибка при загрузке файла", e);
                    }
                })
                .toList();
    }


    // Скачивание файла
    public void downloadFile(Long id,HttpServletResponse response) throws IOException {

        log.info("Получен запрос на скачивание файла с id = {}", id);

        FileContentEntity fileContent = fileContentRepository.findById(id).orElseThrow(() -> {
                    log.warn("Файл с id = {} не найден в базе данных", id);
                    return new EntityNotFoundException("Файл не найден");
        });

        log.info("Путь к файлу на FTP: {}", fileContent.getFtpPath());

        try (InputStream stream = ftpService.downloadFile(fileContent.getFtpPath())) {
            response.setContentType(fileContent.getType());
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=\"" + extractFileName(fileContent.getFtpPath()) + "\""
            );
            stream.transferTo(response.getOutputStream());
            response.flushBuffer();
            log.info("Файл успешно передан клиенту.");
        } catch (IOException e) {
            log.error("Ошибка при скачивании файла: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw e;
        }
    }

    // Просмотр одного файла
    public void viewFile(Long id, HttpServletResponse response) throws IOException {
        FileContentEntity fileContent = fileContentRepository.findById(id).orElseThrow();
        sendFileToResponse(fileContent, response);
    }

    // Просмотр нескольких файлов
    public void viewMultipleFiles(
            String type,
            Long objectId,
            HttpServletResponse response) throws IOException {
        List<FileContentEntity> files = fileContentRepository.findByTypeAndObjectId(type, objectId);
        for (FileContentEntity fileContent : files) {
            sendFileToResponse(fileContent, response);
        }
    }

    private void sendFileToResponse(
            FileContentEntity fileContent,
            HttpServletResponse response) throws IOException {
        response.setContentType(fileContent.getType());
        response.setHeader("Content-Disposition", "inline; filename=\"" + extractFileName(fileContent.getFtpPath()) + "\"");

        try (InputStream stream = ftpService.downloadFile(fileContent.getFtpPath())) {
            stream.transferTo(response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw e;
        }
    }


    @Transactional(readOnly = true)
    public List<FileContent> getAllMetadataFiles() {
        return fileContentRepository.findAll().stream()
                .map(FileContentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FileContent getMetadataById(Long id) {
        FileContentEntity entity = fileContentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FileData not found"));
        return FileContentMapper.toDto(entity);
    }

    // Удаление одного файла
    public void deleteFile(Long id) {
        FileContentEntity entity = fileContentRepository.findById(id).orElseThrow();
        entity.setDeleted(true);
        fileContentRepository.save(entity);
    }


    //Реализовать по надобности
    // Удаление нескольких файлов
    public void deleteMultipleFiles(List<Long> ids) {
        List<FileContentEntity> files = fileContentRepository.findAllById(ids);
        files.forEach(file -> file.setDeleted(true));
        fileContentRepository.saveAll(files);
    }


    private String extractFileName(String ftpPath) {
        return ftpPath.substring(ftpPath.lastIndexOf('/') + 1);
    }


    @Transactional
    public FileContent updateMetadata(Long id, FileContent fileContent) {
        FileContentEntity existing = fileContentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Файл не найден: id = " + id));

        // Обновление нужных полей
        existing.setType(fileContent.getType());

        FileContentEntity updated = fileContentRepository.save(existing);
        return FileContentMapper.toDto(updated);
    }


    private Boolean isReport(String type){
        return type.equals("building");
    }

    @Transactional(readOnly = true)
    public List<FileContent> getFilesMetadataById(String type, Long objectId) {
        return fileContentRepository.findByTypeAndObjectId(type, objectId).stream()
                .map(FileContentMapper::toDto)
                .collect(Collectors.toList());
    }
}