package com.drugprevention.gymbowlingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Properties;

@SpringBootApplication
public class GymBowlingBackendApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GymBowlingBackendApplication.class);
        
        // Set default profile to render if no profile is specified
        Properties props = new Properties();
        props.setProperty("spring.profiles.active", "render");
        app.setDefaultProperties(props);
        
        app.run(args);
    }

}
