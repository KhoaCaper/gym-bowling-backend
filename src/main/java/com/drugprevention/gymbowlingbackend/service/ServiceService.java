package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.dto.ServiceDTO;
import com.drugprevention.gymbowlingbackend.dto.CreateServiceDTO;
import com.drugprevention.gymbowlingbackend.entity.ServiceType;
import com.drugprevention.gymbowlingbackend.entity.Center;
import com.drugprevention.gymbowlingbackend.repository.ServiceRepository;
import com.drugprevention.gymbowlingbackend.repository.ServiceTypeRepository;
import com.drugprevention.gymbowlingbackend.repository.CenterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Service
 * Manages gym and bowling services
 * 
 * @author GymBo Team
 * @version 1.0
 */
@Service
@Transactional
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final CenterRepository centerRepository;

    public ServiceService(ServiceRepository serviceRepository, 
                         ServiceTypeRepository serviceTypeRepository,
                         CenterRepository centerRepository) {
        this.serviceRepository = serviceRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.centerRepository = centerRepository;
    }

    /**
     * Get all active services
     * 
     * @return List of active services
     */
    public List<ServiceDTO> getAllActiveServices() {
        return serviceRepository.findByIsActiveTrue()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get all services (including inactive)
     * 
     * @return List of all services
     */
    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get services by center
     * 
     * @param centerId Center ID
     * @return List of services for the center
     */
    public List<ServiceDTO> getServicesByCenter(Long centerId) {
        validateCenterExists(centerId);
        
        return serviceRepository.findByCenterIdAndIsActiveTrue(centerId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get services by service type
     * 
     * @param serviceTypeId Service type ID
     * @return List of services for the service type
     */
    public List<ServiceDTO> getServicesByServiceType(Long serviceTypeId) {
        validateServiceTypeExists(serviceTypeId);
        
        return serviceRepository.findByServiceTypeIdAndIsActiveTrue(serviceTypeId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get services by center and service type
     * 
     * @param centerId Center ID
     * @param serviceTypeId Service type ID
     * @return List of services for the center and service type
     */
    public List<ServiceDTO> getServicesByCenterAndServiceType(Long centerId, Long serviceTypeId) {
        validateCenterExists(centerId);
        validateServiceTypeExists(serviceTypeId);
        
        return serviceRepository.findByCenterIdAndServiceTypeIdAndIsActiveTrue(centerId, serviceTypeId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get service by ID
     * 
     * @param id Service ID
     * @return Optional service DTO
     */
    public Optional<ServiceDTO> getServiceById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Service ID must be positive");
        }
        
        return serviceRepository.findById(id)
            .map(this::convertToDTO);
    }

    /**
     * Create new service
     * 
     * @param createServiceDTO Service creation data
     * @return Created service DTO
     */
    public ServiceDTO createService(CreateServiceDTO createServiceDTO) {
        validateCreateServiceRequest(createServiceDTO);
        
        // Validate service type exists
        ServiceType serviceType = validateServiceTypeExists(createServiceDTO.getServiceTypeId());

        // Validate center exists
        Center center = validateCenterExists(createServiceDTO.getCenterId());

        // Validate price
        validatePrice(createServiceDTO.getPrice());

        // Check if service with same name already exists in the center
        if (serviceRepository.existsByNameAndCenterId(createServiceDTO.getName(), createServiceDTO.getCenterId())) {
            throw new IllegalStateException("Service with name '" + createServiceDTO.getName() + 
                "' already exists in center '" + center.getName() + "'");
        }

        com.drugprevention.gymbowlingbackend.entity.Service service = new com.drugprevention.gymbowlingbackend.entity.Service(
            serviceType,
            center,
            createServiceDTO.getName(),
            createServiceDTO.getDescription(),
            createServiceDTO.getPrice()
        );

        com.drugprevention.gymbowlingbackend.entity.Service savedService = serviceRepository.save(service);
        return convertToDTO(savedService);
    }

    /**
     * Update existing service
     * 
     * @param id Service ID
     * @param updateServiceDTO Service update data
     * @return Updated service DTO
     */
    public ServiceDTO updateService(Long id, CreateServiceDTO updateServiceDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Service ID must be positive");
        }
        
        validateCreateServiceRequest(updateServiceDTO);
        
        return serviceRepository.findById(id)
            .map(existingService -> {
                // Validate service type exists
                ServiceType serviceType = validateServiceTypeExists(updateServiceDTO.getServiceTypeId());

                // Validate center exists
                Center center = validateCenterExists(updateServiceDTO.getCenterId());

                // Validate price
                validatePrice(updateServiceDTO.getPrice());

                // Check if service with same name already exists in the center (excluding current service)
                if (!existingService.getName().equals(updateServiceDTO.getName()) &&
                    serviceRepository.existsByNameAndCenterId(updateServiceDTO.getName(), updateServiceDTO.getCenterId())) {
                    throw new IllegalStateException("Service with name '" + updateServiceDTO.getName() + 
                        "' already exists in center '" + center.getName() + "'");
                }

                // Update service
                existingService.setServiceType(serviceType);
                existingService.setCenter(center);
                existingService.setName(updateServiceDTO.getName());
                existingService.setDescription(updateServiceDTO.getDescription());
                existingService.setPrice(updateServiceDTO.getPrice());
                existingService.setUpdatedAt(LocalDateTime.now());

                com.drugprevention.gymbowlingbackend.entity.Service savedService = serviceRepository.save(existingService);
                return convertToDTO(savedService);
            })
            .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));
    }

    /**
     * Delete service
     * 
     * @param id Service ID
     */
    public void deleteService(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Service ID must be positive");
        }
        
        com.drugprevention.gymbowlingbackend.entity.Service service = serviceRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));
        
        // Check if service is used in any package plans
        if (!service.getPackagePlanDetails().isEmpty()) {
            throw new IllegalStateException("Cannot delete service that is used in package plans");
        }
        
        serviceRepository.deleteById(id);
    }

    /**
     * Toggle service status (active/inactive)
     * 
     * @param id Service ID
     * @return Updated service DTO
     */
    public ServiceDTO toggleServiceStatus(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Service ID must be positive");
        }
        
        return serviceRepository.findById(id)
            .map(service -> {
                service.setIsActive(!service.getIsActive());
                service.setUpdatedAt(LocalDateTime.now());
                
                com.drugprevention.gymbowlingbackend.entity.Service savedService = serviceRepository.save(service);
                return convertToDTO(savedService);
            })
            .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));
    }

    /**
     * Validate create service request
     */
    private void validateCreateServiceRequest(CreateServiceDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Service data cannot be null");
        }
        
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Service name is required");
        }
        
        if (dto.getServiceTypeId() == null || dto.getServiceTypeId() <= 0) {
            throw new IllegalArgumentException("Service type ID must be positive");
        }
        
        if (dto.getCenterId() == null || dto.getCenterId() <= 0) {
            throw new IllegalArgumentException("Center ID must be positive");
        }
        
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Service price must be positive");
        }
    }

    /**
     * Validate service type exists
     */
    private ServiceType validateServiceTypeExists(Long serviceTypeId) {
        return serviceTypeRepository.findById(serviceTypeId)
            .orElseThrow(() -> new IllegalArgumentException("Service type not found with id: " + serviceTypeId));
    }

    /**
     * Validate center exists
     */
    private Center validateCenterExists(Long centerId) {
        return centerRepository.findById(centerId)
            .orElseThrow(() -> new IllegalArgumentException("Center not found with id: " + centerId));
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
     * Convert Service entity to DTO
     */
    private ServiceDTO convertToDTO(com.drugprevention.gymbowlingbackend.entity.Service service) {
        return new ServiceDTO(
            service.getId(),
            service.getServiceType().getId(),
            service.getServiceType().getName(),
            service.getCenter().getId(),
            service.getCenter().getName(),
            service.getName(),
            service.getDescription(),
            service.getPrice(),
            service.getIsActive(),
            service.getCreatedAt()
        );
    }
}
