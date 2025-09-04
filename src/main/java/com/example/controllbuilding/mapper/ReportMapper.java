package com.example.controllbuilding.mapper;

import com.example.controllbuilding.model.DTO.Report;
import com.example.controllbuilding.model.entity.ReportEntity;
import com.example.controllbuilding.model.entity.TaskEntity;

public class ReportMapper {
    public static Report toDto(ReportEntity entity){
        if (entity == null) {
            return null;
        }
        Report dto = new Report();
        dto.setId(entity.getId());
        dto.setTaskId(entity.getTaskEntity() != null ? entity.getTaskEntity().getId() : null);
        dto.setComment(entity.getComment());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        return dto;
    }

    public static ReportEntity toEntity(Report dto){
        if (dto == null) {
            return null;
        }
        ReportEntity entity = new ReportEntity();
        entity.setId(dto.getId());
        entity.setComment(dto.getComment());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return entity;
    }

    public static ReportEntity toEntityWithRelations(Report dto, TaskEntity task){
        ReportEntity entity = toEntity(dto);

        entity.setTaskEntity(task);
        return entity;
    }
}
