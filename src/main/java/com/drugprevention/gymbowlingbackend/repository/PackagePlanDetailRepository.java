package com.drugprevention.gymbowlingbackend.repository;

import com.drugprevention.gymbowlingbackend.entity.PackagePlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackagePlanDetailRepository extends JpaRepository<PackagePlanDetail, Long> {
    
    // Find by package plan ID
    List<PackagePlanDetail> findByPackagePlanId(Long packagePlanId);
    
    // Find by service ID
    List<PackagePlanDetail> findByServiceId(Long serviceId);
    
    // Find by package plan ID and service ID
    Optional<PackagePlanDetail> findByPackagePlanIdAndServiceId(Long packagePlanId, Long serviceId);
    
    // Check if exists by package plan ID and service ID
    boolean existsByPackagePlanIdAndServiceId(Long packagePlanId, Long serviceId);
    
    // Custom query to get all details with package plan and service info
    @Query("SELECT ppd FROM PackagePlanDetail ppd " +
           "JOIN FETCH ppd.packagePlan pp " +
           "JOIN FETCH ppd.service s " +
           "WHERE pp.id = :packagePlanId")
    List<PackagePlanDetail> findByPackagePlanIdWithDetails(@Param("packagePlanId") Long packagePlanId);
    
    // Delete all by package plan ID
    void deleteByPackagePlanId(Long packagePlanId);
    
    // Delete all by service ID
    void deleteByServiceId(Long serviceId);
}
