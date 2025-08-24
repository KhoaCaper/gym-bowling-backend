package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.CreatePackageDTO;
import com.drugprevention.gymbowlingbackend.dto.CreatePackageDetailDTO;
import com.drugprevention.gymbowlingbackend.dto.CreateCompletePackageDTO;
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

/**
 * PackagePlan Controller
 * Quản lý các gói dịch vụ (Package Plans) và chi tiết services trong gói
 * 
 * @author Gym Bowling Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/package-plans")
@CrossOrigin(origins = "*")
public class PackagePlanController {

    @Autowired
    private PackagePlanService packagePlanService;

    @Autowired
    private PackagePlanDetailService packagePlanDetailService;

    /**
     * Tạo gói dịch vụ mới
     * 
     * @param dto Thông tin gói dịch vụ cần tạo
     * @return Gói dịch vụ đã được tạo
     * 
     * @apiNote Staff sử dụng API này để tạo gói dịch vụ cơ bản
     * @summary Tạo gói dịch vụ mới
     */
    @PostMapping
    public ResponseEntity<?> createPackagePlan(@RequestBody CreatePackageDTO dto) {
        try {
            PackagePlan packagePlan = packagePlanService.createPackage(dto);
            return ResponseEntity.ok(packagePlan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Tạo gói dịch vụ hoàn chỉnh với services và timeframes
     * 
     * @param dto Thông tin gói dịch vụ và services cần tạo
     * @return Gói dịch vụ hoàn chỉnh đã được tạo
     * 
     * @apiNote Staff sử dụng API này để tạo gói dịch vụ hoàn chỉnh với services
     * @summary Tạo gói dịch vụ hoàn chỉnh
     */
    @PostMapping("/complete")
    public ResponseEntity<?> createCompletePackage(@RequestBody CreateCompletePackageDTO dto) {
        try {
            PackagePlan packagePlan = packagePlanService.createCompletePackage(dto);
            return ResponseEntity.ok(packagePlan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách tất cả gói dịch vụ
     * 
     * @return Danh sách gói dịch vụ
     * 
     * @apiNote User sử dụng API này để xem danh sách gói dịch vụ có sẵn
     * @summary Lấy danh sách gói dịch vụ
     */
    @GetMapping
    public ResponseEntity<List<PackagePlanDTO>> getAllPackagePlans() {
        List<PackagePlanDTO> packagePlans = packagePlanService.getAllPackages();
        return ResponseEntity.ok(packagePlans);
    }

    /**
     * Lấy thông tin gói dịch vụ theo ID
     * 
     * @param id ID của gói dịch vụ
     * @return Thông tin gói dịch vụ
     * 
     * @apiNote User sử dụng API này để xem chi tiết gói dịch vụ cụ thể
     * @summary Lấy thông tin gói dịch vụ theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<PackagePlanDTO> packagePlan = packagePlanService.getPackageById(id);
        if (packagePlan.isPresent()) {
            return ResponseEntity.ok(packagePlan.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cập nhật thông tin gói dịch vụ
     * 
     * @param id ID của gói dịch vụ cần cập nhật
     * @param updatedPackage Thông tin gói dịch vụ mới
     * @return Gói dịch vụ đã được cập nhật
     * 
     * @apiNote Staff sử dụng API này để cập nhật thông tin gói dịch vụ
     * @summary Cập nhật gói dịch vụ
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePackagePlan(@PathVariable Long id, @RequestBody PackagePlan updatedPackage) {
        try {
            PackagePlan updated = packagePlanService.updatePackage(id, updatedPackage);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Xóa gói dịch vụ
     * 
     * @param id ID của gói dịch vụ cần xóa
     * @return Thông báo xóa thành công
     * 
     * @apiNote Staff sử dụng API này để xóa gói dịch vụ không còn sử dụng
     * @summary Xóa gói dịch vụ
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackagePlan(@PathVariable Long id) {
        try {
            packagePlanService.deletePackage(id);
            return ResponseEntity.ok().body("Package plan deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Tạo gói dịch vụ hoàn chỉnh với nhiều services và timeframes
     * 
     * @param packagePlanId ID của gói dịch vụ
     * @param details Danh sách services và timeframes cần thêm vào gói
     * @return Danh sách PackagePlanDetail đã được tạo
     * 
     * @apiNote Staff sử dụng API này để thêm services vào gói dịch vụ
     * @summary Thêm services vào gói dịch vụ
     */
    @PostMapping("/{packagePlanId}/services")
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

    /**
     * Lấy danh sách services trong gói dịch vụ
     * 
     * @param packagePlanId ID của gói dịch vụ
     * @return Danh sách PackagePlanDetail (services + timeframes)
     * 
     * @apiNote User sử dụng API này để xem services có trong gói dịch vụ
     * @summary Lấy services trong gói dịch vụ
     */
    @GetMapping("/{packagePlanId}/services")
    public ResponseEntity<List<PackagePlanDetail>> getPackageDetails(@PathVariable Long packagePlanId) {
        List<PackagePlanDetail> details = packagePlanDetailService.getDetailsByPackagePlan(packagePlanId);
        return ResponseEntity.ok(details);
    }
}



