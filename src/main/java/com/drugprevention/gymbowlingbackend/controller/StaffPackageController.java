package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.PackagePlanDTO;
import com.drugprevention.gymbowlingbackend.dto.CreatePackageDTO;
import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.service.PackagePlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Staff Package Management Controller
 * Handles package plan operations for staff members
 * 
 * @author GymBo Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/staff/packages")
@Tag(name = "Staff Package Management", description = "APIs for staff to manage package plans")
@CrossOrigin(origins = "*")
public class StaffPackageController {

    private final PackagePlanService packagePlanService;

    public StaffPackageController(PackagePlanService packagePlanService) {
        this.packagePlanService = packagePlanService;
    }

    /**
     * Get all package plans (including inactive ones)
     * 
     * @return List of all package plans
     */
    @GetMapping
    @Operation(
        summary = "Get all packages", 
        description = "Get all packages including inactive ones"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved packages",
            content = @Content(schema = @Schema(implementation = PackagePlanDTO.class))
        )
    })
    public ResponseEntity<Map<String, Object>> getAllPackages() {
        try {
            List<PackagePlanDTO> packages = packagePlanService.getAllPackages();
            // The service already returns DTOs, so we can use them directly
            return ResponseEntity.ok(Map.of(
                "packages", packages,
                "count", packages.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to retrieve packages: " + e.getMessage()));
        }
    }

    /**
     * Create new package plan
     * 
     * @param createPackageDTO Package creation data
     * @return Created package plan
     */
    @PostMapping
    @Operation(
        summary = "Create new package", 
        description = "Create a new package plan with services"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Package created successfully",
            content = @Content(schema = @Schema(implementation = PackagePlanDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input or validation failed",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, Object>> createPackage(@RequestBody CreatePackageDTO createPackageDTO) {
        try {
            PackagePlan createdPackage = packagePlanService.createPackage(createPackageDTO);
            PackagePlanDTO packageDTO = packagePlanService.getPackageById(createdPackage.getId())
                .orElseThrow(() -> new RuntimeException("Failed to convert created package to DTO"));
            
            return ResponseEntity.ok(Map.of(
                "message", "Package created successfully",
                "package", packageDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create package: " + e.getMessage()));
        }
    }

    /**
     * Update existing package plan
     * 
     * @param id Package plan ID
     * @param updatedPackage Updated package plan data
     * @return Updated package plan
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update package", 
        description = "Update an existing package plan"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Package updated successfully",
            content = @Content(schema = @Schema(implementation = PackagePlanDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input or validation failed",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Package not found",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, Object>> updatePackage(@PathVariable Long id, @RequestBody PackagePlan updatedPackage) {
        try {
            PackagePlan updated = packagePlanService.updatePackage(id, updatedPackage);
            PackagePlanDTO packageDTO = packagePlanService.getPackageById(updated.getId())
                .orElseThrow(() -> new RuntimeException("Failed to convert updated package to DTO"));
            
            return ResponseEntity.ok(Map.of(
                "message", "Package updated successfully",
                "package", packageDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update package: " + e.getMessage()));
        }
    }

    /**
     * Delete package plan
     * 
     * @param id Package plan ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete package", 
        description = "Delete a package plan by ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Package deleted successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Package not found or cannot be deleted",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, String>> deletePackage(@PathVariable Long id) {
        try {
            packagePlanService.deletePackage(id);
            return ResponseEntity.ok(Map.of("message", "Package deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete package: " + e.getMessage()));
        }
    }

    /**
     * Toggle package plan status (active/inactive)
     * 
     * @param id Package plan ID
     * @return Updated package plan
     */
    @PatchMapping("/{id}/toggle-status")
    @Operation(
        summary = "Toggle package status", 
        description = "Toggle package plan active/inactive status"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Package status updated successfully",
            content = @Content(schema = @Schema(implementation = PackagePlanDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Package not found",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, Object>> togglePackageStatus(@PathVariable Long id) {
        try {
            PackagePlan updatedPackage = packagePlanService.togglePackageStatus(id);
            PackagePlanDTO packageDTO = packagePlanService.getPackageById(updatedPackage.getId())
                .orElseThrow(() -> new RuntimeException("Failed to convert updated package to DTO"));
            
            return ResponseEntity.ok(Map.of(
                "message", "Package status updated successfully",
                "package", packageDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update package status: " + e.getMessage()));
        }
    }
}
