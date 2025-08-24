package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.CreatePackageDetailDTO;
import com.drugprevention.gymbowlingbackend.dto.PackagePlanDetailDTO;
import com.drugprevention.gymbowlingbackend.entity.PackagePlanDetail;
import com.drugprevention.gymbowlingbackend.service.PackagePlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * PackagePlanDetail Controller
 * Quản lý chi tiết services và timeframes trong gói dịch vụ
 * 
 * @author Gym Bowling Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/package-plan-details")
@CrossOrigin(origins = "*")
public class PackagePlanDetailController {

    @Autowired
    private PackagePlanDetailService packagePlanDetailService;

    /**
     * Tạo chi tiết gói dịch vụ mới
     * 
     * @param dto Thông tin chi tiết gói dịch vụ cần tạo
     * @return Chi tiết gói dịch vụ đã được tạo
     * 
     * @apiNote Staff sử dụng API này để thêm service cụ thể vào gói dịch vụ
     * @summary Tạo chi tiết gói dịch vụ
     */
    @PostMapping
    public ResponseEntity<?> createPackagePlanDetail(@RequestBody CreatePackageDetailDTO dto) {
        try {
            PackagePlanDetail detail = packagePlanDetailService.createPackagePlanDetail(dto);
            return ResponseEntity.ok(detail);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách chi tiết theo gói dịch vụ
     * 
     * @param packagePlanId ID của gói dịch vụ
     * @return Danh sách chi tiết gói dịch vụ
     * 
     * @apiNote User sử dụng API này để xem services trong gói dịch vụ cụ thể
     * @summary Lấy chi tiết theo gói dịch vụ
     */
    @GetMapping("/package/{packagePlanId}")
    public ResponseEntity<List<PackagePlanDetail>> getDetailsByPackagePlan(@PathVariable Long packagePlanId) {
        List<PackagePlanDetail> details = packagePlanDetailService.getDetailsByPackagePlan(packagePlanId);
        return ResponseEntity.ok(details);
    }

    /**
     * Lấy chi tiết gói dịch vụ theo ID
     * 
     * @param id ID của chi tiết gói dịch vụ
     * @return Chi tiết gói dịch vụ với thông tin đầy đủ
     * 
     * @apiNote User sử dụng API này để xem chi tiết cụ thể của service trong gói
     * @summary Lấy chi tiết gói dịch vụ theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<PackagePlanDetailDTO> detail = packagePlanDetailService.getByIdAsDTO(id);
        if (detail.isPresent()) {
            return ResponseEntity.ok(detail.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cập nhật chi tiết gói dịch vụ
     * 
     * @param id ID của chi tiết gói dịch vụ cần cập nhật
     * @param dto Thông tin chi tiết gói dịch vụ mới
     * @return Chi tiết gói dịch vụ đã được cập nhật
     * 
     * @apiNote Staff sử dụng API này để cập nhật thông tin service trong gói
     * @summary Cập nhật chi tiết gói dịch vụ
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePackagePlanDetail(@PathVariable Long id, @RequestBody CreatePackageDetailDTO dto) {
        try {
            PackagePlanDetail updated = packagePlanDetailService.updatePackagePlanDetail(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Xóa chi tiết gói dịch vụ
     * 
     * @param id ID của chi tiết gói dịch vụ cần xóa
     * @return Thông báo xóa thành công
     * 
     * @apiNote Staff sử dụng API này để xóa service khỏi gói dịch vụ
     * @summary Xóa chi tiết gói dịch vụ
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackagePlanDetail(@PathVariable Long id) {
        try {
            packagePlanDetailService.deletePackagePlanDetail(id);
            return ResponseEntity.ok().body("Package plan detail deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Lấy tất cả chi tiết gói dịch vụ
     * 
     * @return Danh sách tất cả chi tiết gói dịch vụ
     * 
     * @apiNote Staff sử dụng API này để quản lý tất cả chi tiết gói dịch vụ
     * @summary Lấy tất cả chi tiết gói dịch vụ
     */
    @GetMapping
    public ResponseEntity<List<PackagePlanDetail>> getAllPackagePlanDetails() {
        List<PackagePlanDetail> details = packagePlanDetailService.getAllPackagePlanDetails();
        return ResponseEntity.ok(details);
    }

    /**
     * Lấy chi tiết gói dịch vụ theo service
     * 
     * @param serviceId ID của service
     * @return Danh sách chi tiết gói dịch vụ chứa service này
     * 
     * @apiNote Staff sử dụng API này để xem service được sử dụng trong những gói nào
     * @summary Lấy chi tiết theo service
     */
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<PackagePlanDetail>> getDetailsByService(@PathVariable Long serviceId) {
        // This will be implemented in the service
        return ResponseEntity.ok().build();
    }

    /**
     * Lấy chi tiết gói dịch vụ theo timeframe
     * 
     * @param timeFrameId ID của timeframe
     * @return Danh sách chi tiết gói dịch vụ sử dụng timeframe này
     * 
     * @apiNote Staff sử dụng API này để xem timeframe được sử dụng trong những gói nào
     * @summary Lấy chi tiết theo timeframe
     */
    @GetMapping("/timeframe/{timeFrameId}")
    public ResponseEntity<List<PackagePlanDetail>> getDetailsByTimeFrame(@PathVariable Long timeFrameId) {
        // This will be implemented in the service
        return ResponseEntity.ok().build();
    }
}
