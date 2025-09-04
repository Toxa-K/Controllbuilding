package com.example.controllbuilding.service;

import com.example.controllbuilding.mapper.ReportMapper;
import com.example.controllbuilding.model.DTO.Report;
import com.example.controllbuilding.model.entity.ReportEntity;
import com.example.controllbuilding.model.entity.TaskEntity;
import com.example.controllbuilding.repository.ReportRepository;
import com.example.controllbuilding.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final TaskRepository taskRepository;




    @Transactional(readOnly = true)
    public List<Report> getAll() {
        return reportRepository.findAll().stream()
                .map(ReportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Report getById(Long id) {
        ReportEntity entity = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        return ReportMapper.toDto(entity);
    }

    @Transactional
    public Report create(Report report) {
        TaskEntity task = report.getTaskId() != null ?
                taskRepository.findById(report.getTaskId())
                        .orElseThrow(() -> new RuntimeException("Task not found")) :
                null;
        ReportEntity entity = ReportMapper.toEntity(report);
        entity.setTaskEntity(task);
        return  ReportMapper.toDto(reportRepository.save(entity));
    }

    @Transactional
    public Report update(Long id, Report report) {
        ReportEntity existingEntity = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        existingEntity.setId(report.getId());
        existingEntity.setComment(report.getComment());
        existingEntity.setStartDate(report.getStartDate());
        existingEntity.setEndDate(report.getEndDate());

        if (report.getTaskId() != null){
            TaskEntity task = taskRepository.findById(report.getTaskId())
                    .orElseThrow(() -> new RuntimeException("Task not found"));
            existingEntity.setTaskEntity(task);
        }
        return ReportMapper.toDto(reportRepository.save(existingEntity));
    }

    @Transactional
    public void delete(Long id) {
        reportRepository.deleteById(id);
    }

    public Report getByTaskId(Long taskId) {
        ReportEntity entity = reportRepository.findByTaskEntityId(taskId)
                .orElseThrow(() -> new RuntimeException("Report not found for taskId: " + taskId));
        return ReportMapper.toDto(entity);
    }
}
