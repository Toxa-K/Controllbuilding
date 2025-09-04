package com.example.controllbuilding.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "building")
public class BuildingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "building_seq")
    @SequenceGenerator(name = "building_seq", sequenceName = "building_building_id_seq", allocationSize = 1)
    @Column(name = "building_id")
    private Long id;

    private String name;
    private String status;
    private String address;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "buildingEntity", cascade = CascadeType.ALL)
    private List<TaskEntity> taskEntities;

    @OneToMany(mappedBy = "buildingEntity", cascade = CascadeType.ALL)
    private List<FileContentEntity> fileContentEntities;
}
