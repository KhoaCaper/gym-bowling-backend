package com.drugprevention.gymbowlingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymBowlingBackendApplication {

    public static void main(String[] args) {
        // Check environment to determine profile - PRIORITIZE RAILWAY
        String env = System.getenv("SPRING_PROFILES_ACTIVE");
        String railwayEnv = System.getenv("RAILWAY_ENVIRONMENT");
        String port = System.getenv("PORT");
        
        if (railwayEnv != null || (port != null && !port.isEmpty())) {
            // Running on Railway - use railway profile
            System.setProperty("spring.profiles.active", "railway");
            System.out.println("🚂 Running in RAILWAY mode (PORT=" + port + ", ENV=" + railwayEnv + ")");
        } else if (env != null && !env.isEmpty()) {
            // Profile specified via environment variable
            System.setProperty("spring.profiles.active", env);
            System.out.println("⚙️ Running with profile: " + env);
        } else {
            // Local development - use local profile
            System.setProperty("spring.profiles.active", "local");
            System.out.println("💻 Running in LOCAL development mode");
        }
        
        SpringApplication.run(GymBowlingBackendApplication.class, args);
    }

}
