package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.PackagePlanDTO;
import com.drugprevention.gymbowlingbackend.dto.CreatePackageDTO;
import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.service.PackagePlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/package-plans")
@Tag(name = "Package Management", description = "APIs for managing gym/bowling packages")
public class PackageController {

    private final PackagePlanService packagePlanService;

    public PackageController(PackagePlanService packagePlanService) {
        this.packagePlanService = packagePlanService;
    }

    @GetMapping
    @Operation(summary = "Get all active packages", 
               description = "Retrieve all active gym/bowling packages sorted by price")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved packages")
    public ResponseEntity<List<PackagePlanDTO>> getAllActivePackages() {
        List<PackagePlanDTO> packages = packagePlanService.getAllActivePackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get package by ID", 
               description = "Retrieve a specific package by its ID")
    @ApiResponse(responseCode = "200", description = "Package found")
    @ApiResponse(responseCode = "404", description = "Package not found")
    public ResponseEntity<PackagePlanDTO> getPackageById(@PathVariable Long id) {
        return packagePlanService.getPackageById(id)
            .map(packagePlan -> ResponseEntity.ok(packagePlan))
            .orElse(ResponseEntity.notFound().build());
    }
}
