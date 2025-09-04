package com.example.controllbuilding.mapper;

import com.example.controllbuilding.model.DTO.BuildingDto;
import com.example.controllbuilding.model.entity.BuildingEntity;

public class BuildingMapper {
    public static BuildingDto toDto(BuildingEntity entity) {
        if (entity == null) {
            return null;
        }
        BuildingDto dto = new BuildingDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setAddress(entity.getAddress());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        return dto;
    }
    public static BuildingEntity toEntity(BuildingDto dto){
        if (dto == null) {
            return null;
        }
        BuildingEntity entity = new BuildingEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        entity.setAddress(dto.getAddress());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return entity;
    }
}
