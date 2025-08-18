package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.service.PackagePlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackagePlanService packagePlanService;

    public PackageController(PackagePlanService packagePlanService) {
        this.packagePlanService = packagePlanService;
    }

    @GetMapping
    public ResponseEntity<List<PackagePlan>> getAllActivePackages() {
        List<PackagePlan> packages = packagePlanService.getAllActivePackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageById(@PathVariable Long id) {
        return packagePlanService.getPackageById(id)
            .map(packagePlan -> ResponseEntity.ok(packagePlan))
            .orElse(ResponseEntity.notFound().build());
    }
}
