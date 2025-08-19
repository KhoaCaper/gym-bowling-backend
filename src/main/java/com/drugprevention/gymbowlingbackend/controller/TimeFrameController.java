package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.TimeFrameDTO;
import com.drugprevention.gymbowlingbackend.dto.CreateTimeFrameDTO;
import com.drugprevention.gymbowlingbackend.service.TimeFrameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timeframes")
@Tag(name = "Time Frame Management", description = "APIs for managing service time frames")
public class TimeFrameController {

    private final TimeFrameService timeFrameService;

    public TimeFrameController(TimeFrameService timeFrameService) {
        this.timeFrameService = timeFrameService;
    }

    @GetMapping
    @Operation(summary = "Get all available time frames", 
               description = "Retrieve all available time frames")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved time frames",
                content = @Content(schema = @Schema(implementation = TimeFrameDTO.class)))
    public ResponseEntity<List<TimeFrameDTO>> getAllAvailableTimeFrames() {
        List<TimeFrameDTO> timeFrames = timeFrameService.getAllAvailableTimeFrames();
        return ResponseEntity.ok(timeFrames);
    }

    @GetMapping("/center/{centerId}")
    @Operation(summary = "Get time frames by center", 
               description = "Get all time frames for a specific center")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved time frames for center")
    public ResponseEntity<List<TimeFrameDTO>> getTimeFramesByCenter(@PathVariable Long centerId) {
        List<TimeFrameDTO> timeFrames = timeFrameService.getTimeFramesByCenter(centerId);
        return ResponseEntity.ok(timeFrames);
    }

    @GetMapping("/center/{centerId}/day/{dayOfWeek}")
    @Operation(summary = "Get time frames by center and day", 
               description = "Get all time frames for a specific center and day of week")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved time frames")
    @ApiResponse(responseCode = "400", description = "Invalid day of week")
    public ResponseEntity<List<TimeFrameDTO>> getTimeFramesByCenterAndDay(
            @PathVariable Long centerId, 
            @PathVariable String dayOfWeek) {
        try {
            List<TimeFrameDTO> timeFrames = timeFrameService.getTimeFramesByCenterAndDay(centerId, dayOfWeek);
            return ResponseEntity.ok(timeFrames);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(List.of()); // Return empty list for invalid day
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get time frame by ID", 
               description = "Get a specific time frame by its ID")
    @ApiResponse(responseCode = "200", description = "Time frame found")
    @ApiResponse(responseCode = "404", description = "Time frame not found")
    public ResponseEntity<TimeFrameDTO> getTimeFrameById(@PathVariable Long id) {
        return timeFrameService.getTimeFrameById(id)
            .map(timeFrame -> ResponseEntity.ok(timeFrame))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new time frame", 
               description = "Create a new time frame for a center")
    @ApiResponse(responseCode = "200", description = "Time frame created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or validation failed")
    public ResponseEntity<?> createTimeFrame(@RequestBody CreateTimeFrameDTO createTimeFrameDTO) {
        try {
            LocalTime start = LocalTime.parse(createTimeFrameDTO.getStartTime());
            LocalTime end = LocalTime.parse(createTimeFrameDTO.getEndTime());
            
            TimeFrameDTO createdTimeFrame = timeFrameService.createTimeFrame(
                createTimeFrameDTO.getCenterId(), 
                createTimeFrameDTO.getDayOfWeek(), 
                start, 
                end
            );
            return ResponseEntity.ok(Map.of(
                "message", "Time frame created successfully",
                "timeFrame", createdTimeFrame
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create time frame: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update time frame", 
               description = "Update an existing time frame by ID")
    @ApiResponse(responseCode = "200", description = "Time frame updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or validation failed")
    @ApiResponse(responseCode = "404", description = "Time frame not found")
    public ResponseEntity<?> updateTimeFrame(
            @PathVariable Long id,
            @RequestBody CreateTimeFrameDTO updateTimeFrameDTO) {
        try {
            LocalTime start = LocalTime.parse(updateTimeFrameDTO.getStartTime());
            LocalTime end = LocalTime.parse(updateTimeFrameDTO.getEndTime());
            
            TimeFrameDTO updatedTimeFrame = timeFrameService.updateTimeFrame(
                id, 
                updateTimeFrameDTO.getDayOfWeek(), 
                start, 
                end
            );
            return ResponseEntity.ok(Map.of(
                "message", "Time frame updated successfully",
                "timeFrame", updatedTimeFrame
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update time frame: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete time frame", 
               description = "Delete a time frame by ID")
    @ApiResponse(responseCode = "200", description = "Time frame deleted successfully")
    @ApiResponse(responseCode = "400", description = "Time frame not found")
    public ResponseEntity<?> deleteTimeFrame(@PathVariable Long id) {
        try {
            timeFrameService.deleteTimeFrame(id);
            return ResponseEntity.ok(Map.of("message", "Time frame deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete time frame: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/toggle-availability")
    @Operation(summary = "Toggle time frame availability", 
               description = "Toggle time frame available/unavailable status")
    @ApiResponse(responseCode = "200", description = "Time frame availability updated successfully")
    @ApiResponse(responseCode = "400", description = "Time frame not found")
    public ResponseEntity<?> toggleTimeFrameAvailability(@PathVariable Long id) {
        try {
            TimeFrameDTO updatedTimeFrame = timeFrameService.toggleTimeFrameAvailability(id);
            return ResponseEntity.ok(Map.of(
                "message", "Time frame availability updated successfully",
                "timeFrame", updatedTimeFrame
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update time frame availability: " + e.getMessage()));
        }
    }
}
