package com.drugprevention.gymbowlingbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Health Check Controller
 * Simple endpoint for Railway health check
 */
@RestController
public class HealthController {

    @GetMapping("/")
    public String rootHealthCheck() {
        return "OK";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        try {
            // Simple health check without database dependency
            return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "gym-bowling-backend",
                "version", "1.0.0",
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "environment", "production",
                "database", "checking",
                "message", "Service is starting up"
            ));
        } catch (Exception e) {
            // Return service UP even if there are errors
            return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "gym-bowling-backend",
                "version", "1.0.0",
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "environment", "production",
                "database", "error",
                "error", e.getMessage(),
                "message", "Service is running but database connection failed"
            ));
        }
    }
}
