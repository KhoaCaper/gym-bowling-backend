package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.dto.PackagePlanDTO;
import com.drugprevention.gymbowlingbackend.dto.CreatePackageDTO;
import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.repository.PackagePlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PackagePlanService {

    private final PackagePlanRepository packagePlanRepository;

    public PackagePlanService(PackagePlanRepository packagePlanRepository) {
        this.packagePlanRepository = packagePlanRepository;
    }

    public List<PackagePlanDTO> getAllActivePackages() {
        return packagePlanRepository.findByIsActiveTrueOrderByPriceAsc()
            .stream()
            .distinct()  // Remove duplicates if any
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<PackagePlanDTO> getAllPackages() {
        return packagePlanRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public Optional<PackagePlanDTO> getPackageById(Long id) {
        return packagePlanRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    private PackagePlanDTO convertToDTO(PackagePlan packagePlan) {
        return new PackagePlanDTO(
            packagePlan.getId(),
            packagePlan.getName(),
            packagePlan.getDescription(),
            packagePlan.getPrice(),
            packagePlan.getDurationMonths(),
            packagePlan.getIsActive(),
            packagePlan.getCreatedAt()
        );
    }

    public PackagePlan createPackage(PackagePlan packagePlan) {
        return packagePlanRepository.save(packagePlan);
    }

    public PackagePlan updatePackage(Long id, PackagePlan updatedPackage) {
        return packagePlanRepository.findById(id)
            .map(existingPackage -> {
                existingPackage.setName(updatedPackage.getName());
                existingPackage.setDescription(updatedPackage.getDescription());
                existingPackage.setPrice(updatedPackage.getPrice());
                existingPackage.setDurationMonths(updatedPackage.getDurationMonths());
                existingPackage.setIsActive(updatedPackage.getIsActive());
                return packagePlanRepository.save(existingPackage);
            })
            .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
    }

    public void deletePackage(Long id) {
        if (packagePlanRepository.existsById(id)) {
            packagePlanRepository.deleteById(id);
        } else {
            throw new RuntimeException("Package not found with id: " + id);
        }
    }

    public PackagePlan togglePackageStatus(Long id) {
        return packagePlanRepository.findById(id)
            .map(packagePlan -> {
                packagePlan.setIsActive(!packagePlan.getIsActive());
                return packagePlanRepository.save(packagePlan);
            })
            .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
    }
}
