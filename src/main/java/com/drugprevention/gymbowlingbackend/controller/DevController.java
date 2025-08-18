package com.drugprevention.gymbowlingbackend.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dev")
public class DevController {

    private final FirebaseAuth firebaseAuth;

    public DevController(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @PostMapping("/create-custom-token")
    public ResponseEntity<?> createCustomToken(@RequestBody Map<String, String> request) {
        try {
            String uid = request.getOrDefault("uid", "admin-firebase-uid");
            
            // Create custom token for testing
            String customToken = firebaseAuth.createCustomToken(uid);
            
            return ResponseEntity.ok(Map.of(
                "customToken", customToken,
                "message", "Use this token to get ID token from Firebase",
                "instructions", Map.of(
                    "step1", "Use Firebase signInWithCustomToken(customToken)",
                    "step2", "Then call getIdToken() to get the ID token",
                    "step3", "Use ID token in Swagger Authorization"
                )
            ));
            
        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create custom token: " + e.getMessage()));
        }
    }

    @GetMapping("/firebase-users")
    public ResponseEntity<?> listFirebaseUsers() {
        try {
            // List first 10 users for testing
            var listUsersResult = firebaseAuth.listUsers(null, 10);
            
            java.util.List<Map<String, String>> usersList = new java.util.ArrayList<>();
            for (var user : listUsersResult.getValues()) {
                usersList.add(Map.of(
                    "uid", user.getUid(),
                    "email", user.getEmail() != null ? user.getEmail() : "no-email",
                    "displayName", user.getDisplayName() != null ? user.getDisplayName() : "no-name"
                ));
            }
            
            return ResponseEntity.ok(Map.of(
                "users", usersList,
                "message", "Firebase users list"
            ));
            
        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to list users: " + e.getMessage()));
        }
    }
}
