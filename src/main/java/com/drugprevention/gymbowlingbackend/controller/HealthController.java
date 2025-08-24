package com.drugprevention.gymbowlingbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Health Check Controller
 * Includes database connectivity check for local testing
 */
@RestController
public class HealthController {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String home() {
        return "Gym Bowling Backend is running!";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/test")
    public String test() {
        return "Hello from Gym Bowling Backend!";
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        try {
            // Check database connection if available
            String dbStatus = "unknown";
            if (jdbcTemplate != null) {
                try {
                    jdbcTemplate.queryForObject("SELECT 1", Integer.class);
                    dbStatus = "connected";
                } catch (Exception e) {
                    dbStatus = "error";
                }
            } else {
                dbStatus = "unavailable";
            }

            // Get current environment from Spring profiles
            String currentProfile = System.getProperty("spring.profiles.active");
            if (currentProfile == null) {
                currentProfile = "unknown";
            }
            
            return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "gym-bowling-backend",
                "version", "1.0.0",
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "environment", currentProfile,
                "database", dbStatus,
                "message", "Service is running"
            ));
        } catch (Exception e) {
            // Get current environment from Spring profiles
            String currentProfile = System.getProperty("spring.profiles.active");
            if (currentProfile == null) {
                currentProfile = "unknown";
            }
            
            return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "gym-bowling-backend",
                "version", "1.0.0",
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "environment", currentProfile,
                "database", "error",
                "error", e.getMessage(),
                "message", "Service is running but health check failed"
            ));
        }
    }
}
