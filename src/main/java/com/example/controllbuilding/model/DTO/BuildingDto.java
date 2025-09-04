package com.example.controllbuilding.model.DTO;


import lombok.Data;

import java.time.LocalDate;

@Data
public class BuildingDto {
    private Long id;
    private String name;
    private String status;
    private String address;
    private LocalDate startDate;
    private LocalDate endDate;
}
