package com.drugprevention.gymbowlingbackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.file:}")
    private String firebaseConfigFile;
    
    @Value("${GOOGLE_APPLICATION_CREDENTIALS_JSON:}")
    private String firebaseCredentialsJson;

    @PostConstruct
    public void initialize() {
        try {
            GoogleCredentials credentials = null;
            
            // Try to use JSON from environment variable first (for Railway/cloud deployment)
            if (firebaseCredentialsJson != null && !firebaseCredentialsJson.trim().isEmpty()) {
                InputStream credentialsStream = new ByteArrayInputStream(firebaseCredentialsJson.getBytes());
                credentials = GoogleCredentials.fromStream(credentialsStream);
                System.out.println("Firebase initialized from environment variable");
            }
            // Fallback to file (for local development)
            else if (firebaseConfigFile != null && !firebaseConfigFile.trim().isEmpty()) {
                InputStream serviceAccount = new ClassPathResource(firebaseConfigFile).getInputStream();
                credentials = GoogleCredentials.fromStream(serviceAccount);
                System.out.println("Firebase initialized from config file: " + firebaseConfigFile);
            }
            
            if (credentials != null) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
            } else {
                System.out.println("No Firebase credentials found. Skipping Firebase initialization.");
            }
            
        } catch (IOException e) {
            System.err.println("Failed to initialize Firebase: " + e.getMessage());
            // For development, continue without Firebase if config file not found
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                System.err.println("Firebase not initialized, returning null FirebaseAuth");
                return null;
            }
            return FirebaseAuth.getInstance();
        } catch (Exception e) {
            System.err.println("Firebase not initialized, returning null FirebaseAuth");
            return null;
        }
    }
}
