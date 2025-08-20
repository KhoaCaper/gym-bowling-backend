package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.dto.PackagePlanDTO;
import com.drugprevention.gymbowlingbackend.dto.CreatePackageDTO;
import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.entity.Center;
import com.drugprevention.gymbowlingbackend.entity.PackagePlanDetail;
import com.drugprevention.gymbowlingbackend.repository.PackagePlanRepository;
import com.drugprevention.gymbowlingbackend.repository.CenterRepository;
import com.drugprevention.gymbowlingbackend.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Package Plan Service
 * Manages gym and bowling package plans
 * 
 * @author GymBo Team
 * @version 1.0
 */
@Service
@Transactional
public class PackagePlanService {

    private final PackagePlanRepository packagePlanRepository;
    private final CenterRepository centerRepository;
    private final ServiceRepository serviceRepository;

    public PackagePlanService(PackagePlanRepository packagePlanRepository, 
                             CenterRepository centerRepository,
                             ServiceRepository serviceRepository) {
        this.packagePlanRepository = packagePlanRepository;
        this.centerRepository = centerRepository;
        this.serviceRepository = serviceRepository;
    }

    /**
     * Get all active package plans
     * 
     * @return List of active package plans
     */
    public List<PackagePlanDTO> getAllActivePackages() {
        return packagePlanRepository.findByIsActiveTrueOrderByPriceAsc()
            .stream()
            .distinct()  // Remove duplicates if any
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get all package plans (including inactive)
     * 
     * @return List of all package plans
     */
    public List<PackagePlanDTO> getAllPackages() {
        return packagePlanRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get package plan by ID
     * 
     * @param id Package plan ID
     * @return Optional package plan DTO
     */
    public Optional<PackagePlanDTO> getPackageById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Package plan ID must be positive");
        }
        
        return packagePlanRepository.findById(id)
            .map(this::convertToDTO);
    }

    /**
     * Create new package plan from services
     * 
     * @param createPackageDTO Package creation data
     * @return Created package plan
     */
    public PackagePlan createPackage(CreatePackageDTO createPackageDTO) {
        validateCreatePackageRequest(createPackageDTO);
        
        // Validate center exists
        Center center = centerRepository.findById(createPackageDTO.getCenterId())
            .orElseThrow(() -> new IllegalArgumentException("Center not found with id: " + createPackageDTO.getCenterId()));

        // Validate price
        validatePrice(createPackageDTO.getPrice());

        // Validate duration
        validateDuration(createPackageDTO.getDurationMonths());

        // Check if package with same name already exists in the center
        if (packagePlanRepository.existsByNameAndCenterId(createPackageDTO.getName(), createPackageDTO.getCenterId())) {
            throw new IllegalStateException("Package plan with name '" + createPackageDTO.getName() + 
                "' already exists in center '" + center.getName() + "'");
        }

        // Create package plan
        PackagePlan packagePlan = new PackagePlan(
            createPackageDTO.getName(),
            createPackageDTO.getDescription(),
            createPackageDTO.getPrice(),
            createPackageDTO.getDurationMonths(),
            center
        );

        // Save package plan
        PackagePlan savedPackagePlan = packagePlanRepository.save(packagePlan);

        // Create package plan details if services are provided
        if (createPackageDTO.getServiceIds() != null && !createPackageDTO.getServiceIds().isEmpty()) {
            createPackagePlanDetails(savedPackagePlan, createPackageDTO.getServiceIds());
        }

        return savedPackagePlan;
    }

    /**
     * Update existing package plan
     * 
     * @param id Package plan ID
     * @param updatedPackage Updated package plan data
     * @return Updated package plan
     */
    public PackagePlan updatePackage(Long id, PackagePlan updatedPackage) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Package plan ID must be positive");
        }
        
        return packagePlanRepository.findById(id)
            .map(existingPackage -> {
                // Validate price
                if (updatedPackage.getPrice() != null) {
                    validatePrice(updatedPackage.getPrice());
                }

                // Validate duration
                if (updatedPackage.getDurationMonths() != null) {
                    validateDuration(updatedPackage.getDurationMonths());
                }

                // Check if package with same name already exists in the center (excluding current package)
                if (updatedPackage.getName() != null && !existingPackage.getName().equals(updatedPackage.getName()) &&
                    packagePlanRepository.existsByNameAndCenterId(updatedPackage.getName(), existingPackage.getCenter().getId())) {
                    throw new IllegalStateException("Package plan with name '" + updatedPackage.getName() + 
                        "' already exists in center '" + existingPackage.getCenter().getName() + "'");
                }

                // Update package plan
                if (updatedPackage.getName() != null) {
                    existingPackage.setName(updatedPackage.getName());
                }
                if (updatedPackage.getDescription() != null) {
                    existingPackage.setDescription(updatedPackage.getDescription());
                }
                if (updatedPackage.getPrice() != null) {
                    existingPackage.setPrice(updatedPackage.getPrice());
                }
                if (updatedPackage.getDurationMonths() != null) {
                    existingPackage.setDurationMonths(updatedPackage.getDurationMonths());
                }
                if (updatedPackage.getIsActive() != null) {
                    existingPackage.setIsActive(updatedPackage.getIsActive());
                }
                
                existingPackage.setUpdatedAt(LocalDateTime.now());
                
                return packagePlanRepository.save(existingPackage);
            })
            .orElseThrow(() -> new IllegalArgumentException("Package plan not found with id: " + id));
    }

    /**
     * Delete package plan
     * 
     * @param id Package plan ID
     */
    public void deletePackage(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Package plan ID must be positive");
        }
        
        PackagePlan packagePlan = packagePlanRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Package plan not found with id: " + id));
        
        // Check if package plan is used in any orders
        if (!packagePlan.getOrderPackages().isEmpty()) {
            throw new IllegalStateException("Cannot delete package plan that is used in orders");
        }
        
        packagePlanRepository.deleteById(id);
    }

    /**
     * Toggle package plan status (active/inactive)
     * 
     * @param id Package plan ID
     * @return Updated package plan
     */
    public PackagePlan togglePackageStatus(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Package plan ID must be positive");
        }
        
        return packagePlanRepository.findById(id)
            .map(packagePlan -> {
                packagePlan.setIsActive(!packagePlan.getIsActive());
                packagePlan.setUpdatedAt(LocalDateTime.now());
                return packagePlanRepository.save(packagePlan);
            })
            .orElseThrow(() -> new IllegalArgumentException("Package plan not found with id: " + id));
    }

    /**
     * Create package plan details from service IDs
     */
    private void createPackagePlanDetails(PackagePlan packagePlan, List<Long> serviceIds) {
        for (Long serviceId : serviceIds) {
            com.drugprevention.gymbowlingbackend.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + serviceId));
            
            // Check if service belongs to the same center
            if (!service.getCenter().getId().equals(packagePlan.getCenter().getId())) {
                throw new IllegalStateException("Service '" + service.getName() + 
                    "' does not belong to center '" + packagePlan.getCenter().getName() + "'");
            }
            
            // Create package plan detail with default sessions included
            PackagePlanDetail detail = new PackagePlanDetail(packagePlan, service, 1);
            // Note: You'll need to implement PackagePlanDetail repository to save this
        }
    }

    /**
     * Validate create package request
     */
    private void validateCreatePackageRequest(CreatePackageDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Package data cannot be null");
        }
        
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Package name is required");
        }
        
        if (dto.getCenterId() == null || dto.getCenterId() <= 0) {
            throw new IllegalArgumentException("Center ID must be positive");
        }
        
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Package price must be positive");
        }
        
        if (dto.getDurationMonths() == null || dto.getDurationMonths() <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
    }

    /**
     * Validate price
     */
    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        
        if (price.compareTo(new BigDecimal("1000000000")) > 0) {
            throw new IllegalArgumentException("Price cannot exceed 1 billion");
        }
    }

    /**
     * Validate duration
     */
    private void validateDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        
        if (duration > 60) { // Maximum 5 years
            throw new IllegalArgumentException("Duration cannot exceed 60 months");
        }
    }

    /**
     * Convert PackagePlan entity to DTO
     */
    private PackagePlanDTO convertToDTO(PackagePlan packagePlan) {
        return new PackagePlanDTO(
            packagePlan.getId(),
            packagePlan.getName(),
            packagePlan.getDescription(),
            packagePlan.getPrice(),
            packagePlan.getDurationMonths(),
            packagePlan.getIsActive(),
            packagePlan.getCreatedAt(),
            packagePlan.getCenter() != null ? packagePlan.getCenter().getId() : null,
            packagePlan.getCenter() != null ? packagePlan.getCenter().getName() : null
        );
    }
}
