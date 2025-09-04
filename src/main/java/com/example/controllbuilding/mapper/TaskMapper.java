package com.example.controllbuilding.mapper;
import com.example.controllbuilding.model.DTO.Task;
import com.example.controllbuilding.model.entity.BuildingEntity;
import com.example.controllbuilding.model.entity.TaskEntity;
import com.example.controllbuilding.model.entity.UserEntity;

public class TaskMapper {
    public static Task toDto(TaskEntity entity) {
        if (entity == null) return null;
        Task dto = new Task();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setAddress(entity.getAddress());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setParentTask(entity.getParentTask() != null ? entity.getParentTask().getId() : null);
        dto.setBuildingId(entity.getBuildingEntity() != null ? entity.getBuildingEntity().getId() : null);
        dto.setUserId(entity.getUserEntity() != null ? entity.getUserEntity().getId() : null);
        return dto;
    }

    public static TaskEntity toEntity(Task dto) {
        if (dto == null) return null;

        TaskEntity entity = new TaskEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        entity.setAddress(dto.getAddress());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        if (dto.getParentTask() != null) {
            TaskEntity parent = new TaskEntity();
            parent.setId(dto.getParentTask());
            entity.setParentTask(parent);
        }
        return entity;
    }

    public static TaskEntity toEntityWithRelations(Task dto, UserEntity user, BuildingEntity building) {
        TaskEntity entity = toEntity(dto); // Используем базовую версию

        // Устанавливаем связи
        entity.setUserEntity(user);
        entity.setBuildingEntity(building);


        return entity;
    }
}
