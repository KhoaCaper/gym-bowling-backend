package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.entity.ServiceType;
import com.drugprevention.gymbowlingbackend.service.ServiceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-types")
@Tag(name = "Service Type Management", description = "APIs for managing service types")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @GetMapping
    @Operation(summary = "Get all active service types", 
               description = "Retrieve all active service types")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved service types",
                content = @Content(schema = @Schema(implementation = ServiceType.class)))
    public ResponseEntity<List<ServiceType>> getAllActiveServiceTypes() {
        List<ServiceType> serviceTypes = serviceTypeService.getAllActiveServiceTypes();
        return ResponseEntity.ok(serviceTypes);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all service types", 
               description = "Retrieve all service types including inactive ones")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all service types")
    public ResponseEntity<List<ServiceType>> getAllServiceTypes() {
        List<ServiceType> serviceTypes = serviceTypeService.getAllServiceTypes();
        return ResponseEntity.ok(serviceTypes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get service type by ID", 
               description = "Retrieve a specific service type by its ID")
    @ApiResponse(responseCode = "200", description = "Service type found")
    @ApiResponse(responseCode = "404", description = "Service type not found")
    public ResponseEntity<ServiceType> getServiceTypeById(@PathVariable Long id) {
        return serviceTypeService.getServiceTypeById(id)
            .map(serviceType -> ResponseEntity.ok(serviceType))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get service type by name", 
               description = "Retrieve a specific service type by its name")
    @ApiResponse(responseCode = "200", description = "Service type found")
    @ApiResponse(responseCode = "404", description = "Service type not found")
    public ResponseEntity<ServiceType> getServiceTypeByName(@PathVariable String name) {
        return serviceTypeService.getServiceTypeByName(name)
            .map(serviceType -> ResponseEntity.ok(serviceType))
            .orElse(ResponseEntity.notFound().build());
    }
}
