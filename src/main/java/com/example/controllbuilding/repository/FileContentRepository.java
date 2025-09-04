package com.example.controllbuilding.repository;

import com.example.controllbuilding.model.entity.FileContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileContentRepository  extends JpaRepository<FileContentEntity, Long> {

    List<FileContentEntity> findByTypeAndObjectId(String type, Long objectId);

}