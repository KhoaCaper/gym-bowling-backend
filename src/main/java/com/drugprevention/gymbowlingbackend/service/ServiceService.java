package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.dto.ServiceDTO;
import com.drugprevention.gymbowlingbackend.dto.CreateServiceDTO;
import com.drugprevention.gymbowlingbackend.entity.ServiceType;
import com.drugprevention.gymbowlingbackend.entity.Center;
import com.drugprevention.gymbowlingbackend.repository.ServiceRepository;
import com.drugprevention.gymbowlingbackend.repository.ServiceTypeRepository;
import com.drugprevention.gymbowlingbackend.repository.CenterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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

    public List<ServiceDTO> getAllActiveServices() {
        return serviceRepository.findByIsActiveTrue()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ServiceDTO> getServicesByCenter(Long centerId) {
        return serviceRepository.findByCenterIdAndIsActiveTrue(centerId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ServiceDTO> getServicesByServiceType(Long serviceTypeId) {
        return serviceRepository.findByServiceTypeIdAndIsActiveTrue(serviceTypeId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ServiceDTO> getServicesByCenterAndServiceType(Long centerId, Long serviceTypeId) {
        return serviceRepository.findByCenterIdAndServiceTypeIdAndIsActiveTrue(centerId, serviceTypeId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public Optional<ServiceDTO> getServiceById(Long id) {
        return serviceRepository.findById(id)
            .map(this::convertToDTO);
    }

    public ServiceDTO createService(CreateServiceDTO createServiceDTO) {
        // Validate service type exists
        ServiceType serviceType = serviceTypeRepository.findById(createServiceDTO.getServiceTypeId())
            .orElseThrow(() -> new RuntimeException("Service type not found with id: " + createServiceDTO.getServiceTypeId()));

        // Validate center exists
        Center center = centerRepository.findById(createServiceDTO.getCenterId())
            .orElseThrow(() -> new RuntimeException("Center not found with id: " + createServiceDTO.getCenterId()));

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

    public ServiceDTO updateService(Long id, CreateServiceDTO updateServiceDTO) {
        return serviceRepository.findById(id)
            .map(existingService -> {
                // Validate service type exists
                ServiceType serviceType = serviceTypeRepository.findById(updateServiceDTO.getServiceTypeId())
                    .orElseThrow(() -> new RuntimeException("Service type not found with id: " + updateServiceDTO.getServiceTypeId()));

                // Validate center exists
                Center center = centerRepository.findById(updateServiceDTO.getCenterId())
                    .orElseThrow(() -> new RuntimeException("Center not found with id: " + updateServiceDTO.getCenterId()));

                existingService.setServiceType(serviceType);
                existingService.setCenter(center);
                existingService.setName(updateServiceDTO.getName());
                existingService.setDescription(updateServiceDTO.getDescription());
                existingService.setPrice(updateServiceDTO.getPrice());

                com.drugprevention.gymbowlingbackend.entity.Service savedService = serviceRepository.save(existingService);
                return convertToDTO(savedService);
            })
            .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }

    public void deleteService(Long id) {
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
        } else {
            throw new RuntimeException("Service not found with id: " + id);
        }
    }

    public ServiceDTO toggleServiceStatus(Long id) {
        return serviceRepository.findById(id)
            .map(service -> {
                service.setIsActive(!service.getIsActive());
                com.drugprevention.gymbowlingbackend.entity.Service savedService = serviceRepository.save(service);
                return convertToDTO(savedService);
            })
            .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }

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
