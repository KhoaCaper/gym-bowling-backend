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
 * Smart endpoint for Railway health check with database connectivity
 */
@RestController
public class HealthController {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String rootHealthCheck() {
        return "OK";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/ready")
    public String ready() {
        return "ready";
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

            return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "gym-bowling-backend",
                "version", "1.0.0",
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "environment", "production",
                "database", dbStatus,
                "message", "Service is running"
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
                "message", "Service is running but health check failed"
            ));
        }
    }
}
