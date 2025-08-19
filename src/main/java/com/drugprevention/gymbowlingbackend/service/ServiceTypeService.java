package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.entity.ServiceType;
import com.drugprevention.gymbowlingbackend.repository.ServiceTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeService(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    public List<ServiceType> getAllActiveServiceTypes() {
        return serviceTypeRepository.findByIsActiveTrue();
    }

    public List<ServiceType> getAllServiceTypes() {
        return serviceTypeRepository.findAll();
    }

    public Optional<ServiceType> getServiceTypeById(Long id) {
        return serviceTypeRepository.findById(id);
    }

    public Optional<ServiceType> getServiceTypeByName(String name) {
        return serviceTypeRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return serviceTypeRepository.existsByName(name);
    }
}
