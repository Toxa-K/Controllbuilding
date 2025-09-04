package com.example.controllbuilding.repository;


import com.example.controllbuilding.model.entity.ReportEntity;
import com.example.controllbuilding.model.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    Optional<ReportEntity> findByTaskEntityId(Long taskId);

}
