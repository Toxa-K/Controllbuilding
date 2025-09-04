package com.example.controllbuilding.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "report")
@ToString(exclude = {"taskEntity"})
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_seq")
    @SequenceGenerator(name = "report_seq", sequenceName = "report_report_id_seq", allocationSize = 1)
    @Column(name = "report_id")
    private Long id;

    private String comment;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskEntity taskEntity;

    @OneToMany(mappedBy = "reportEntity", cascade = CascadeType.ALL)
    private List<FileContentEntity> fileContentEntities;
}