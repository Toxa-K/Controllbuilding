package com.example.controllbuilding.model.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Report {
    private Long id;
    private Long taskId; // ID корневой задачи
    private String comment;
    private LocalDate startDate;
    private LocalDate endDate;
}
