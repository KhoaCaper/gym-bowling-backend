package com.drugprevention.gymbowlingbackend.repository;

import com.drugprevention.gymbowlingbackend.entity.Center;
import com.drugprevention.gymbowlingbackend.entity.Service;
import com.drugprevention.gymbowlingbackend.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    List<Service> findByIsActiveTrue();
    
    List<Service> findByServiceType(ServiceType serviceType);
    
    List<Service> findByCenter(Center center);
    
    List<Service> findByServiceTypeAndCenter(ServiceType serviceType, Center center);
    
    List<Service> findByIsActiveTrueAndServiceType(ServiceType serviceType);
    
    List<Service> findByIsActiveTrueAndCenter(Center center);
}
