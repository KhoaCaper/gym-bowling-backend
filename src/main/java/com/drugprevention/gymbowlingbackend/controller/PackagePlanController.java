package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.CreatePackageDTO;
import com.drugprevention.gymbowlingbackend.dto.CreatePackageDetailDTO;
import com.drugprevention.gymbowlingbackend.dto.PackagePlanDTO;
import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.entity.PackagePlanDetail;
import com.drugprevention.gymbowlingbackend.service.PackagePlanService;
import com.drugprevention.gymbowlingbackend.service.PackagePlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/package-plan-details")
@CrossOrigin(origins = "*")
public class PackagePlanController {

    @Autowired
    private PackagePlanService packagePlanService;

    @Autowired
    private PackagePlanDetailService packagePlanDetailService;

    // Create new package plan
    @PostMapping("/create-package")
    public ResponseEntity<?> createPackagePlan(@RequestBody CreatePackageDTO dto) {
        try {
            PackagePlan packagePlan = packagePlanService.createPackage(dto);
            return ResponseEntity.ok(packagePlan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get all package plans
    @GetMapping("/packages")
    public ResponseEntity<List<PackagePlanDTO>> getAllPackagePlans() {
        List<PackagePlanDTO> packagePlans = packagePlanService.getAllPackages();
        return ResponseEntity.ok(packagePlans);
    }

    // Get package plan by id
    @GetMapping("/packages/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<PackagePlanDTO> packagePlan = packagePlanService.getPackageById(id);
        if (packagePlan.isPresent()) {
            return ResponseEntity.ok(packagePlan.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update package plan
    @PutMapping("/packages/{id}")
    public ResponseEntity<?> updatePackagePlan(@PathVariable Long id, @RequestBody PackagePlan updatedPackage) {
        try {
            PackagePlan updated = packagePlanService.updatePackage(id, updatedPackage);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Delete package plan
    @DeleteMapping("/packages/{id}")
    public ResponseEntity<?> deletePackagePlan(@PathVariable Long id) {
        try {
            packagePlanService.deletePackage(id);
            return ResponseEntity.ok().body("Package plan deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Create complete package with multiple services at different timeframes
    @PostMapping("/packages/{packagePlanId}/create-complete")
    public ResponseEntity<?> createCompletePackage(
            @PathVariable Long packagePlanId,
            @RequestBody List<CreatePackageDetailDTO> details) {
        try {
            List<PackagePlanDetail> createdDetails = new ArrayList<>();
            
            for (CreatePackageDetailDTO detailDto : details) {
                // Set the package plan ID from path
                detailDto.setPackagePlanId(packagePlanId);
                
                // Create each detail
                PackagePlanDetail detail = packagePlanDetailService.createPackagePlanDetail(detailDto);
                createdDetails.add(detail);
            }
            
            return ResponseEntity.ok(createdDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error creating complete package: " + e.getMessage());
        }
    }

    // Get all details for a package plan
    @GetMapping("/packages/{packagePlanId}/details")
    public ResponseEntity<List<PackagePlanDetail>> getPackageDetails(@PathVariable Long packagePlanId) {
        List<PackagePlanDetail> details = packagePlanDetailService.getDetailsByPackagePlan(packagePlanId);
        return ResponseEntity.ok(details);
    }
}



