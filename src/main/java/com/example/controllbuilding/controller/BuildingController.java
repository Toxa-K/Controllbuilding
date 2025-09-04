package com.example.controllbuilding.controller;


import com.example.controllbuilding.model.DTO.BuildingDto;
import com.example.controllbuilding.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/buildings")
public class BuildingController {

    private final BuildingService buildingService;

    // Получение всех объектов
    @GetMapping
    public ResponseEntity<List<BuildingDto>> getAllBuildings() {
        List<BuildingDto> buildingDtos = buildingService.getAll();
        return ResponseEntity.ok(buildingDtos);
    }

    // Получение объекта по ID
    @GetMapping("/{id}")
    public ResponseEntity<BuildingDto> getBuildingById(@PathVariable Long id) {
        BuildingDto buildingDto = buildingService.getById(id);
        return ResponseEntity.ok(buildingDto);
    }

    // Создание нового объекта
    @PostMapping
    public ResponseEntity<BuildingDto> createBuilding(@RequestBody BuildingDto buildingDto) {
        BuildingDto createdBuildingDto = buildingService.create(buildingDto);
        return new ResponseEntity<>(createdBuildingDto, HttpStatus.CREATED);
    }

    // Обновление объекта
    @PutMapping("/{id}")
    public ResponseEntity<BuildingDto> updateBuilding(
            @PathVariable Long id,
            @RequestBody BuildingDto buildingDto) {
        BuildingDto updatedBuildingDto = buildingService.update(id, buildingDto);
        return ResponseEntity.ok(updatedBuildingDto);
    }

    // Удаление объекта
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBuilding(@PathVariable Long id) {
        buildingService.delete(id);
    }
}