package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String rootHealthCheck() {
        return "OK";
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        try {
            // Test database connection with a simple query
            long userCount = userRepository.count();
            
            return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "gym-bowling-backend",
                "version", "1.0.0",
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "environment", "production",
                "database", "connected",
                "userCount", userCount,
                "message", "Service is healthy and database is connected"
            ));
        } catch (Exception e) {
            // Return service UP but database disconnected
            return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "gym-bowling-backend",
                "version", "1.0.0",
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "environment", "production",
                "database", "disconnected",
                "error", e.getMessage(),
                "message", "Service is running but database connection failed"
            ));
        }
    }
}
