package com.example.controllbuilding.model.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Task {
    private Long id; //ID задачи
    private Long buildingId; // ID объекта
    private Long parentTask; // ID корневой задачи
    private Long userId; // ID пользователя
    private String name;
    private String status;
    private String address;
    private LocalDate startDate;
    private LocalDate endDate;
}

