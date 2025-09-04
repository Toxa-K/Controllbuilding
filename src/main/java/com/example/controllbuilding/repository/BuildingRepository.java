package com.example.controllbuilding.repository;

import com.example.controllbuilding.model.entity.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {


}
