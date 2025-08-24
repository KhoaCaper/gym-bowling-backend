package com.drugprevention.gymbowlingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymBowlingBackendApplication {

    public static void main(String[] args) {
        // Check environment to determine profile
        String env = System.getenv("SPRING_PROFILES_ACTIVE");
        String renderEnv = System.getenv("RENDER");
        
        if (renderEnv != null && renderEnv.equals("true")) {
            // Running on Render - use prod profile
            System.setProperty("spring.profiles.active", "prod");
            System.out.println("🚀 Running in RENDER production mode");
        } else if (System.getenv("RAILWAY") != null) {
            // Running on Railway - use railway profile
            System.setProperty("spring.profiles.active", "railway");
            System.out.println("🚂 Running in RAILWAY production mode");
        } else if (env != null && !env.isEmpty()) {
            // Profile specified via environment variable
            System.setProperty("spring.profiles.active", env);
            System.out.println("⚙️ Running with profile: " + env);
        } else {
            // Check if PORT environment variable exists (production indicator)
            String port = System.getenv("PORT");
            if (port != null && !port.isEmpty()) {
                System.setProperty("spring.profiles.active", "prod");
                System.out.println("🌐 Running in PRODUCTION mode (PORT=" + port + ")");
            } else {
                // Local development - use local profile
                System.setProperty("spring.profiles.active", "local");
                System.out.println("💻 Running in LOCAL development mode");
            }
        }
        
        SpringApplication.run(GymBowlingBackendApplication.class, args);
    }

}
