package com.drugprevention.gymbowlingbackend.repository;

import com.drugprevention.gymbowlingbackend.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByCenterIdAndIsActiveTrue(Long centerId);
    List<Service> findByServiceTypeIdAndIsActiveTrue(Long serviceTypeId);
    List<Service> findByCenterIdAndServiceTypeIdAndIsActiveTrue(Long centerId, Long serviceTypeId);
    List<Service> findByIsActiveTrue();
    
    /**
     * Check if service with given name exists in the specified center
     * 
     * @param name Service name
     * @param centerId Center ID
     * @return true if service exists, false otherwise
     */
    boolean existsByNameAndCenterId(String name, Long centerId);
}
