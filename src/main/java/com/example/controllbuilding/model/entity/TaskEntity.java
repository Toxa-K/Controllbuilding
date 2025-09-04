package com.example.controllbuilding.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "building_tasks")
@ToString(exclude = {"reportEntities"})
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private TaskEntity parentTask;// ID корневой задачи

    private String name;
    private String status;
    private String address;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private BuildingEntity buildingEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "taskEntity", cascade = CascadeType.ALL)
    private List<ReportEntity> reportEntities;



    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL)
    private List<TaskEntity> subTasks;
}