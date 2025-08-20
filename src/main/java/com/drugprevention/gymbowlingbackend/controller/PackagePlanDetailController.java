package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.CreatePackageDetailDTO;
import com.drugprevention.gymbowlingbackend.entity.PackagePlanDetail;
import com.drugprevention.gymbowlingbackend.service.PackagePlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/package-plan-details")
@CrossOrigin(origins = "*")
public class PackagePlanDetailController {

    @Autowired
    private PackagePlanDetailService packagePlanDetailService;

    // Create new package plan detail
    @PostMapping
    public ResponseEntity<?> createPackagePlanDetail(@RequestBody CreatePackageDetailDTO dto) {
        try {
            PackagePlanDetail detail = packagePlanDetailService.createPackagePlanDetail(dto);
            return ResponseEntity.ok(detail);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get all details for a specific package plan
    @GetMapping("/package/{packagePlanId}")
    public ResponseEntity<List<PackagePlanDetail>> getDetailsByPackagePlan(@PathVariable Long packagePlanId) {
        List<PackagePlanDetail> details = packagePlanDetailService.getDetailsByPackagePlan(packagePlanId);
        return ResponseEntity.ok(details);
    }

    // Get package plan detail by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<PackagePlanDetail> detail = packagePlanDetailService.getById(id);
        if (detail.isPresent()) {
            return ResponseEntity.ok(detail.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update package plan detail
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePackagePlanDetail(@PathVariable Long id, @RequestBody CreatePackageDetailDTO dto) {
        try {
            PackagePlanDetail updated = packagePlanDetailService.updatePackagePlanDetail(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Delete package plan detail
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackagePlanDetail(@PathVariable Long id) {
        try {
            packagePlanDetailService.deletePackagePlanDetail(id);
            return ResponseEntity.ok().body("Package plan detail deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get all package plan details
    @GetMapping
    public ResponseEntity<List<PackagePlanDetail>> getAllPackagePlanDetails() {
        List<PackagePlanDetail> details = packagePlanDetailService.getAllPackagePlanDetails();
        return ResponseEntity.ok(details);
    }

    // Get details by service
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<PackagePlanDetail>> getDetailsByService(@PathVariable Long serviceId) {
        // This will be implemented in the service
        return ResponseEntity.ok().build();
    }

    // Get details by time frame
    @GetMapping("/timeframe/{timeFrameId}")
    public ResponseEntity<List<PackagePlanDetail>> getDetailsByTimeFrame(@PathVariable Long timeFrameId) {
        // This will be implemented in the service
        return ResponseEntity.ok().build();
    }
}
