package com.example.controllbuilding.model.DTO;

import lombok.Data;

@Data
public class FileContent {
    private Long id;
    private String name;
    private String ftpPath;
    private Boolean deleted;
    private String type;
    private Long objectId;
    private Long reportId;
    private Long BuildingId;
    private String mimeType;
}
