package com.drugprevention.gymbowlingbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

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
}
