package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.service.PackagePlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff/packages")
public class StaffPackageController {

    private final PackagePlanService packagePlanService;

    public StaffPackageController(PackagePlanService packagePlanService) {
        this.packagePlanService = packagePlanService;
    }

    @GetMapping
    public ResponseEntity<List<PackagePlan>> getAllPackages() {
        List<PackagePlan> packages = packagePlanService.getAllPackages();
        return ResponseEntity.ok(packages);
    }

    @PostMapping
    public ResponseEntity<?> createPackage(@RequestBody PackagePlan packagePlan) {
        try {
            PackagePlan createdPackage = packagePlanService.createPackage(packagePlan);
            return ResponseEntity.ok(Map.of(
                "message", "Package created successfully",
                "package", createdPackage
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create package: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePackage(@PathVariable Long id, @RequestBody PackagePlan packagePlan) {
        try {
            PackagePlan updatedPackage = packagePlanService.updatePackage(id, packagePlan);
            return ResponseEntity.ok(Map.of(
                "message", "Package updated successfully",
                "package", updatedPackage
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update package: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable Long id) {
        try {
            packagePlanService.deletePackage(id);
            return ResponseEntity.ok(Map.of("message", "Package deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete package: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<?> togglePackageStatus(@PathVariable Long id) {
        try {
            PackagePlan updatedPackage = packagePlanService.togglePackageStatus(id);
            return ResponseEntity.ok(Map.of(
                "message", "Package status updated successfully",
                "package", updatedPackage
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update package status: " + e.getMessage()));
        }
    }
}
