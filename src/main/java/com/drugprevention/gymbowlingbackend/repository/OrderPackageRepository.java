package com.drugprevention.gymbowlingbackend.repository;

import com.drugprevention.gymbowlingbackend.entity.Order;
import com.drugprevention.gymbowlingbackend.entity.OrderPackage;
import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPackageRepository extends JpaRepository<OrderPackage, Long> {
    
    List<OrderPackage> findByOrder(Order order);
    
    List<OrderPackage> findByPackagePlan(PackagePlan packagePlan);
    
    List<OrderPackage> findByOrderId(Long orderId);
}
