package com.drugprevention.gymbowlingbackend.repository;

import com.drugprevention.gymbowlingbackend.entity.OrderPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPackageRepository extends JpaRepository<OrderPackage, Long> {
    List<OrderPackage> findByOrderId(Long orderId);
    List<OrderPackage> findByPackagePlanId(Long packagePlanId);
}
