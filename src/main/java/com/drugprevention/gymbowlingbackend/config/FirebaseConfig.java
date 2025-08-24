package com.drugprevention.gymbowlingbackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
public class FirebaseConfig {

    @Value("${firebase.config.file:}")
    private String firebaseConfigFile;
    
    @Value("${GOOGLE_APPLICATION_CREDENTIALS_JSON:}")
    private String firebaseCredentialsJson;

    @Value("${firebase.enabled:false}")
    private boolean firebaseEnabled;

    @PostConstruct
    public void initialize() {
        // Only initialize if Firebase is explicitly enabled
        if (!isFirebaseEnabled()) {
            System.out.println("Firebase is disabled. Skipping initialization.");
            return;
        }
        
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
                try {
                    InputStream serviceAccount = new ClassPathResource(firebaseConfigFile).getInputStream();
                    credentials = GoogleCredentials.fromStream(serviceAccount);
                    System.out.println("Firebase initialized from config file: " + firebaseConfigFile);
                } catch (Exception e) {
                    System.err.println("Firebase config file not found: " + e.getMessage());
                }
            }
            
            if (credentials != null) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                    System.out.println("Firebase app initialized successfully");
                }
            } else {
                System.err.println("No Firebase credentials found. Firebase initialization failed.");
            }
            
        } catch (IOException e) {
            System.err.println("Failed to initialize Firebase: " + e.getMessage());
        }
    }
    
    private boolean isFirebaseEnabled() {
        // Check both environment variable and properties file
        String envFirebaseEnabled = System.getenv("FIREBASE_ENABLED");
        if (envFirebaseEnabled != null) {
            return "true".equalsIgnoreCase(envFirebaseEnabled);
        }
        // Use the value from properties file
        return firebaseEnabled;
    }

    @Bean
    @ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
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
