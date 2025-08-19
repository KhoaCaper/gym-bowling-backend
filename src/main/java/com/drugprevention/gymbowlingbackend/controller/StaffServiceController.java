package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.ServiceDTO;
import com.drugprevention.gymbowlingbackend.dto.CreateServiceDTO;
import com.drugprevention.gymbowlingbackend.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff/services")
@Tag(name = "Staff Service Management", description = "APIs for staff to manage services")
public class StaffServiceController {

    private final ServiceService serviceService;

    public StaffServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    @Operation(summary = "Get all services", 
               description = "Get all services including inactive ones")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved services")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<ServiceDTO> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active services", 
               description = "Get all active services only")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved active services")
    public ResponseEntity<List<ServiceDTO>> getAllActiveServices() {
        List<ServiceDTO> services = serviceService.getAllActiveServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/center/{centerId}")
    @Operation(summary = "Get services by center", 
               description = "Get all services for a specific center")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved services for center")
    public ResponseEntity<List<ServiceDTO>> getServicesByCenter(@PathVariable Long centerId) {
        List<ServiceDTO> services = serviceService.getServicesByCenter(centerId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/service-type/{serviceTypeId}")
    @Operation(summary = "Get services by service type", 
               description = "Get all services for a specific service type")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved services for service type")
    public ResponseEntity<List<ServiceDTO>> getServicesByServiceType(@PathVariable Long serviceTypeId) {
        List<ServiceDTO> services = serviceService.getServicesByServiceType(serviceTypeId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/center/{centerId}/service-type/{serviceTypeId}")
    @Operation(summary = "Get services by center and service type", 
               description = "Get all services for a specific center and service type")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved services")
    public ResponseEntity<List<ServiceDTO>> getServicesByCenterAndServiceType(
            @PathVariable Long centerId, 
            @PathVariable Long serviceTypeId) {
        List<ServiceDTO> services = serviceService.getServicesByCenterAndServiceType(centerId, serviceTypeId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get service by ID", 
               description = "Get a specific service by its ID")
    @ApiResponse(responseCode = "200", description = "Service found")
    @ApiResponse(responseCode = "404", description = "Service not found")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id)
            .map(service -> ResponseEntity.ok(service))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new service", 
               description = "Create a new service for a center")
    @ApiResponse(responseCode = "200", description = "Service created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or validation failed")
    public ResponseEntity<?> createService(@RequestBody CreateServiceDTO createServiceDTO) {
        try {
            ServiceDTO createdService = serviceService.createService(createServiceDTO);
            return ResponseEntity.ok(Map.of(
                "message", "Service created successfully",
                "service", createdService
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create service: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update service", 
               description = "Update an existing service by ID")
    @ApiResponse(responseCode = "200", description = "Service updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or validation failed")
    @ApiResponse(responseCode = "404", description = "Service not found")
    public ResponseEntity<?> updateService(@PathVariable Long id, @RequestBody CreateServiceDTO updateServiceDTO) {
        try {
            ServiceDTO updatedService = serviceService.updateService(id, updateServiceDTO);
            return ResponseEntity.ok(Map.of(
                "message", "Service updated successfully",
                "service", updatedService
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update service: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete service", 
               description = "Delete a service by ID")
    @ApiResponse(responseCode = "200", description = "Service deleted successfully")
    @ApiResponse(responseCode = "400", description = "Service not found")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        try {
            serviceService.deleteService(id);
            return ResponseEntity.ok(Map.of("message", "Service deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete service: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/toggle-status")
    @Operation(summary = "Toggle service status", 
               description = "Toggle service active/inactive status")
    @ApiResponse(responseCode = "200", description = "Service status updated successfully")
    @ApiResponse(responseCode = "400", description = "Service not found")
    public ResponseEntity<?> toggleServiceStatus(@PathVariable Long id) {
        try {
            ServiceDTO updatedService = serviceService.toggleServiceStatus(id);
            return ResponseEntity.ok(Map.of(
                "message", "Service status updated successfully",
                "service", updatedService
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update service status: " + e.getMessage()));
        }
    }
}
