package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.entity.Role;
import com.drugprevention.gymbowlingbackend.repository.UserRepository;
import com.drugprevention.gymbowlingbackend.repository.RoleRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FirebaseAuth firebaseAuth;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, FirebaseAuth firebaseAuth) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.firebaseAuth = firebaseAuth;
    }

    public User createOrUpdateUser(String firebaseUid, String email, String fullName, String phone) {
        Optional<User> existingUser = userRepository.findByFirebaseUid(firebaseUid);
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhone(phone);
            return userRepository.save(user);
        } else {
            User newUser = new User(firebaseUid, email, fullName, phone);
            // Set default role as USER
            Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));
            newUser.setRole(userRole);
            return userRepository.save(newUser);
        }
    }

    public Optional<User> findByFirebaseUid(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getCurrentUser(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User verifyAndGetUser(String token) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            
            return createOrUpdateUser(firebaseUid, email, name, null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Firebase token", e);
        }
    }
}
