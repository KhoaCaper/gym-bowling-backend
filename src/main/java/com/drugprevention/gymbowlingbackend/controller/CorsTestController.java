package com.drugprevention.gymbowlingbackend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cors-test")
@CrossOrigin(origins = "*") // Temporary CORS override for testing
public class CorsTestController {

    @GetMapping("/test")
    public String testCors() {
        return "CORS is working!";
    }

    @PostMapping("/test")
    public String testCorsPost(@RequestBody String body) {
        return "CORS POST is working! Body: " + body;
    }

    @GetMapping("/headers")
    public String testHeaders(@RequestHeader(value = "Origin", required = false) String origin,
                             @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        return String.format("CORS Headers test - Origin: %s, User-Agent: %s", origin, userAgent);
    }
}
