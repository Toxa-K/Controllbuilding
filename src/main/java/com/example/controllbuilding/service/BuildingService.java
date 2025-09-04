package com.example.controllbuilding.service;


import com.example.controllbuilding.mapper.BuildingMapper;
import com.example.controllbuilding.model.DTO.BuildingDto;
import com.example.controllbuilding.model.entity.BuildingEntity;
import com.example.controllbuilding.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuildingService {
    private final BuildingRepository buildingRepository;

    @Transactional(readOnly = true)
    public List<BuildingDto> getAll() {
        return buildingRepository.findAll().stream()
                .map(BuildingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BuildingDto create(BuildingDto buildingDto) {
        BuildingEntity entity = BuildingMapper.toEntity(buildingDto);
        return BuildingMapper.toDto(buildingRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        buildingRepository.deleteById(id);
    }


    @Transactional
    public BuildingDto update(Long id, BuildingDto buildingDto) {
        BuildingEntity entity = buildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Building not found"));

        entity.setId(buildingDto.getId());
        entity.setName(buildingDto.getName());
        entity.setStatus(buildingDto.getStatus());
        entity.setAddress(buildingDto.getAddress());
        entity.setStartDate(buildingDto.getStartDate());
        entity.setEndDate(buildingDto.getEndDate());

        return BuildingMapper.toDto(buildingRepository.save(entity));
    }


    @Transactional(readOnly = true)
    public BuildingDto getById(Long id) {
        BuildingEntity entity = buildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return BuildingMapper.toDto(entity);
    }
}