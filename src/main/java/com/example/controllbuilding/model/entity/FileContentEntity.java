package com.example.controllbuilding.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "file")
public class FileContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_seq")
    @SequenceGenerator(name = "file_seq", sequenceName = "file_file_id_seq", allocationSize = 1)
    @Column(name = "file_id")
    private Long id;
    private String name;
    private String ftpPath;
    private Boolean deleted;
    private String type;
    private Long objectId;
    private String mimeType;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private ReportEntity reportEntity;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private BuildingEntity buildingEntity;
}
