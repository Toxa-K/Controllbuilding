package com.example.controllbuilding.service;

import com.example.controllbuilding.mapper.TaskMapper;
import com.example.controllbuilding.model.DTO.Task;
import com.example.controllbuilding.model.entity.BuildingEntity;
import com.example.controllbuilding.model.entity.TaskEntity;
import com.example.controllbuilding.model.entity.UserEntity;
import com.example.controllbuilding.repository.BuildingRepository;
import com.example.controllbuilding.repository.TaskRepository;
import com.example.controllbuilding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final BuildingRepository buildingRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Task> getAll() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Task getById(Long id) {
        TaskEntity entity = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return TaskMapper.toDto(entity);
    }

    @Transactional
    public Task create(Task task) {
        UserEntity user = task.getUserId() != null ?
                userRepository.findById(task.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found")) :
                null;

        BuildingEntity building = task.getBuildingId() != null ?
                buildingRepository.findById(task.getBuildingId())
                        .orElseThrow(() -> new RuntimeException("Building not found")) :
                null;

        TaskEntity entity = TaskMapper.toEntity(task);
        entity.setUserEntity(user);
        entity.setBuildingEntity(building);

        return TaskMapper.toDto(taskRepository.save(entity));
    }

    @Transactional
    public Task update(Long id, Task task) {
        TaskEntity existingEntity = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Обновляем базовые поля
        existingEntity.setName(task.getName());
        existingEntity.setStatus(task.getStatus());
        existingEntity.setAddress(task.getAddress());
        existingEntity.setStartDate(task.getStartDate());
        existingEntity.setEndDate(task.getEndDate());

        // Обновляем связи
        if (task.getUserId() != null) {
            UserEntity user = userRepository.findById(task.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingEntity.setUserEntity(user);
        }

        if (task.getBuildingId() != null) {
            BuildingEntity building = buildingRepository.findById(task.getBuildingId())
                    .orElseThrow(() -> new RuntimeException("Building not found"));
            existingEntity.setBuildingEntity(building);
        }

        return TaskMapper.toDto(taskRepository.save(existingEntity));
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

}
