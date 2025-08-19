package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.CenterDTO;
import com.drugprevention.gymbowlingbackend.dto.CreateCenterDTO;
import com.drugprevention.gymbowlingbackend.service.CenterService;
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
@RequestMapping("/api/centers")
@Tag(name = "Center Management", description = "APIs for managing gym/bowling centers")
public class CenterController {

    private final CenterService centerService;

    public CenterController(CenterService centerService) {
        this.centerService = centerService;
    }

    @GetMapping
    @Operation(summary = "Get all active centers", 
               description = "Retrieve all active centers sorted by name")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved centers",
                content = @Content(schema = @Schema(implementation = CenterDTO.class)))
    public ResponseEntity<List<CenterDTO>> getAllActiveCenters() {
        List<CenterDTO> centers = centerService.getAllActiveCenters();
        return ResponseEntity.ok(centers);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all centers", 
               description = "Retrieve all centers including inactive ones")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all centers")
    public ResponseEntity<List<CenterDTO>> getAllCenters() {
        List<CenterDTO> centers = centerService.getAllCenters();
        return ResponseEntity.ok(centers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get center by ID", 
               description = "Retrieve a specific center by its ID")
    @ApiResponse(responseCode = "200", description = "Center found")
    @ApiResponse(responseCode = "404", description = "Center not found")
    public ResponseEntity<CenterDTO> getCenterById(@PathVariable Long id) {
        return centerService.getCenterById(id)
            .map(center -> ResponseEntity.ok(center))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new center", 
               description = "Create a new gym/bowling center")
    @ApiResponse(responseCode = "200", description = "Center created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or center name already exists")
    public ResponseEntity<?> createCenter(@RequestBody CreateCenterDTO createCenterDTO) {
        try {
            CenterDTO createdCenter = centerService.createCenter(createCenterDTO);
            return ResponseEntity.ok(Map.of(
                "message", "Center created successfully",
                "center", createdCenter
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create center: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update center", 
               description = "Update an existing center by ID")
    @ApiResponse(responseCode = "200", description = "Center updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or center name already exists")
    @ApiResponse(responseCode = "404", description = "Center not found")
    public ResponseEntity<?> updateCenter(@PathVariable Long id, @RequestBody CreateCenterDTO updateCenterDTO) {
        try {
            CenterDTO updatedCenter = centerService.updateCenter(id, updateCenterDTO);
            return ResponseEntity.ok(Map.of(
                "message", "Center updated successfully",
                "center", updatedCenter
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update center: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete center", 
               description = "Delete a center by ID")
    @ApiResponse(responseCode = "200", description = "Center deleted successfully")
    @ApiResponse(responseCode = "400", description = "Center not found")
    public ResponseEntity<?> deleteCenter(@PathVariable Long id) {
        try {
            centerService.deleteCenter(id);
            return ResponseEntity.ok(Map.of("message", "Center deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete center: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/toggle-status")
    @Operation(summary = "Toggle center status", 
               description = "Toggle center active/inactive status")
    @ApiResponse(responseCode = "200", description = "Center status updated successfully")
    @ApiResponse(responseCode = "400", description = "Center not found")
    public ResponseEntity<?> toggleCenterStatus(@PathVariable Long id) {
        try {
            CenterDTO updatedCenter = centerService.toggleCenterStatus(id);
            return ResponseEntity.ok(Map.of(
                "message", "Center status updated successfully",
                "center", updatedCenter
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update center status: " + e.getMessage()));
        }
    }
}
