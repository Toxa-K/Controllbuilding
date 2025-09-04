package com.example.controllbuilding.controller;

import com.example.controllbuilding.model.DTO.Report;
import com.example.controllbuilding.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    //Получение всех отчетов
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports(){
        List<Report> reports = reportService.getAll();
        return ResponseEntity.ok(reports);
    }

    //Получение отчета по ID
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Report report = reportService.getById(id);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Report> getReportByTaskId(@PathVariable Long id){
        Report report = reportService.getByTaskId(id);
        return ResponseEntity.ok(report);
    }

    //Создание нового отчета
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        Report createdReport = reportService.create(report);
        return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
    }

    //Обновление отчета
    @PutMapping("/{id}")
    public ResponseEntity<Report> updateReport(
            @PathVariable Long id,
            @RequestBody Report report) {
        Report updatedReport = reportService.update(id, report);
        return ResponseEntity.ok(updatedReport);
    }

    //Удаление отчета
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable Long id) {
        reportService.delete(id);
    }
}
