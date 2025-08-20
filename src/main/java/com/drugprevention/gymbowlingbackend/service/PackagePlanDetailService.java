package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.dto.CreatePackageDetailDTO;
import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.entity.PackagePlanDetail;
import com.drugprevention.gymbowlingbackend.repository.PackagePlanDetailRepository;
import com.drugprevention.gymbowlingbackend.repository.PackagePlanRepository;
import com.drugprevention.gymbowlingbackend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PackagePlanDetailService {

    @Autowired
    private PackagePlanDetailRepository packagePlanDetailRepository;

    @Autowired
    private PackagePlanRepository packagePlanRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    // Create new package plan detail
    public PackagePlanDetail createPackagePlanDetail(CreatePackageDetailDTO dto) {
        // Validate package plan exists
        Optional<PackagePlan> packagePlanOpt = packagePlanRepository.findById(dto.getPackagePlanId());
        if (packagePlanOpt.isEmpty()) {
            throw new RuntimeException("Package plan not found with id: " + dto.getPackagePlanId());
        }

        // Validate service exists
        Optional<com.drugprevention.gymbowlingbackend.entity.Service> serviceOpt = serviceRepository.findById(dto.getServiceId());
        if (serviceOpt.isEmpty()) {
            throw new RuntimeException("Service not found with id: " + dto.getServiceId());
        }

        // Create package plan detail
        PackagePlanDetail detail = new PackagePlanDetail(
            packagePlanOpt.get(),
            serviceOpt.get(),
            dto.getSessionsIncluded()
        );

        return packagePlanDetailRepository.save(detail);
    }

    // Get all details for a package plan
    public List<PackagePlanDetail> getDetailsByPackagePlan(Long packagePlanId) {
        return packagePlanDetailRepository.findByPackagePlanId(packagePlanId);
    }

    // Get package plan detail by id
    public Optional<PackagePlanDetail> getById(Long id) {
        return packagePlanDetailRepository.findById(id);
    }

    // Update package plan detail
    public PackagePlanDetail updatePackagePlanDetail(Long id, CreatePackageDetailDTO dto) {
        Optional<PackagePlanDetail> existingOpt = packagePlanDetailRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Package plan detail not found with id: " + id);
        }

        PackagePlanDetail existing = existingOpt.get();
        
        // Update fields
        if (dto.getServiceId() != null) {
            Optional<com.drugprevention.gymbowlingbackend.entity.Service> serviceOpt = serviceRepository.findById(dto.getServiceId());
            if (serviceOpt.isPresent()) {
                existing.setService(serviceOpt.get());
            }
        }

        if (dto.getSessionsIncluded() != null) {
            existing.setSessionsIncluded(dto.getSessionsIncluded());
        }

        return packagePlanDetailRepository.save(existing);
    }

    // Delete package plan detail
    public void deletePackagePlanDetail(Long id) {
        if (!packagePlanDetailRepository.existsById(id)) {
            throw new RuntimeException("Package plan detail not found with id: " + id);
        }
        packagePlanDetailRepository.deleteById(id);
    }

    // Get all package plan details
    public List<PackagePlanDetail> getAllPackagePlanDetails() {
        return packagePlanDetailRepository.findAll();
    }
}
