package com.drugprevention.gymbowlingbackend.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cors-test")
@CrossOrigin(origins = {"http://localhost:5173", "https://ae332185633a.ngrok-free.app"})
public class CorsTestController {

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of(
            "message", "CORS test successful!",
            "timestamp", System.currentTimeMillis(),
            "status", "OK"
        );
    }

    @PostMapping("/test")
    public Map<String, Object> testPost(@RequestBody Map<String, Object> request) {
        return Map.of(
            "message", "POST request successful!",
            "received", request,
            "timestamp", System.currentTimeMillis(),
            "status", "OK"
        );
    }

    @GetMapping("/firebase-test")
    public Map<String, Object> firebaseTest() {
        return Map.of(
            "message", "Firebase endpoint accessible!",
            "firebase_enabled", true,
            "timestamp", System.currentTimeMillis(),
            "status", "OK"
        );
    }
}
