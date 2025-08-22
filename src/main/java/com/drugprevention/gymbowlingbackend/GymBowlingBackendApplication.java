package com.drugprevention.gymbowlingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymBowlingBackendApplication {

    public static void main(String[] args) {
        // Set render profile for production deployment
        System.setProperty("spring.profiles.active", "render");
        SpringApplication.run(GymBowlingBackendApplication.class, args);
    }

}
