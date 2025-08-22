package com.drugprevention.gymbowlingbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String root() {
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
}
