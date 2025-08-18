package com.drugprevention.gymbowlingbackend.repository;

import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackagePlanRepository extends JpaRepository<PackagePlan, Long> {
    List<PackagePlan> findByIsActiveTrue();
    List<PackagePlan> findByIsActiveTrueOrderByPriceAsc();
}
