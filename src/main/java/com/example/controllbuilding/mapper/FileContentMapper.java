package com.example.controllbuilding.mapper;

import com.example.controllbuilding.model.DTO.FileContent;
import com.example.controllbuilding.model.entity.FileContentEntity;

public class FileContentMapper {
    public static FileContent toDto(FileContentEntity entity){
        if (entity == null) {
            return null;
        }
        FileContent dto = new FileContent();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setFtpPath(entity.getFtpPath());
        dto.setDeleted(entity.getDeleted());
        dto.setType(entity.getType());
        dto.setObjectId(entity.getObjectId());
        dto.setMimeType(entity.getMimeType());
        dto.setReportId(entity.getReportEntity() != null ? entity.getReportEntity().getId() : null);
        dto.setBuildingId(entity.getBuildingEntity() != null ? entity.getBuildingEntity().getId() : null);
        return dto;
    }

    public static FileContentEntity toEntity(FileContent dto){
        if (dto == null){
            return null;
        }
        FileContentEntity entity = new FileContentEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setFtpPath(dto.getFtpPath());
        entity.setDeleted(dto.getDeleted());
        entity.setType(dto.getType());
        entity.setObjectId(dto.getObjectId());
        entity.setMimeType(dto.getMimeType());
        return entity;
    }
}
