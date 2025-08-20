package com.drugprevention.gymbowlingbackend.repository;

import com.drugprevention.gymbowlingbackend.entity.PackagePlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackagePlanDetailRepository extends JpaRepository<PackagePlanDetail, Long> {
    List<PackagePlanDetail> findByPackagePlanId(Long packagePlanId);
    List<PackagePlanDetail> findByServiceId(Long serviceId);
}
