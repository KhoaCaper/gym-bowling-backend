package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.entity.Role;
import com.drugprevention.gymbowlingbackend.repository.UserRepository;
import com.drugprevention.gymbowlingbackend.repository.RoleRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FirebaseAuth firebaseAuth;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, FirebaseAuth firebaseAuth, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.firebaseAuth = firebaseAuth;
        this.passwordEncoder = passwordEncoder;
    }

    public User createOrUpdateUser(String firebaseUid, String email, String fullName, String phone) {
        // TEMPORARILY MODIFIED FOR TEAM TESTING - Allow null firebaseUid
        if (firebaseUid != null) {
            Optional<User> existingUser = userRepository.findByFirebaseUid(firebaseUid);
            
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setEmail(email);
                user.setFullName(fullName);
                user.setPhone(phone);
                user.setUpdatedAt(LocalDateTime.now());
                return userRepository.save(user);
            } else {
                // Generate username from email (remove @domain.com)
                String username = email.split("@")[0];
                // Generate a default password and encode it
                String defaultPassword = "default_password_" + System.currentTimeMillis();
                String encodedPassword = passwordEncoder.encode(defaultPassword);
                
                User newUser = new User(username, encodedPassword, firebaseUid, email, fullName, phone);
                // Set default role as USER
                Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Default USER role not found"));
                newUser.setRole(userRole);
                return userRepository.save(newUser);
            }
        } else {
            // FOR TEAM TESTING - Create user without Firebase UID
            String username = "test_user_" + System.currentTimeMillis();
            String defaultPassword = "default_password_" + System.currentTimeMillis();
            String encodedPassword = passwordEncoder.encode(defaultPassword);
            
            User newUser = new User(username, encodedPassword, null, email, fullName, phone);
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
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
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
            
            // Generate username from email
            String username = email.split("@")[0];
            // Generate a default password
            String defaultPassword = "default_password_" + System.currentTimeMillis();
            
            return createOrUpdateUser(firebaseUid, email, name, null); // Pass null for phone
        } catch (Exception e) {
            throw new RuntimeException("Invalid Firebase token", e);
        }
    }

    // Admin methods
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        
        user.setRole(role);
        return userRepository.save(user);
    }

    public User toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        user.setIsActive(!user.getIsActive());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role createRole(String roleName) {
        if (roleRepository.existsByName(roleName)) {
            throw new RuntimeException("Role already exists: " + roleName);
        }
        
        Role newRole = new Role(roleName, "Role for " + roleName);
        return roleRepository.save(newRole);
    }

    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        
        // Check if role is in use by finding users with this role
        List<User> usersWithRole = userRepository.findAll().stream()
            .filter(user -> user.getRole().getId().equals(roleId))
            .toList();
        
        if (!usersWithRole.isEmpty()) {
            throw new RuntimeException("Cannot delete role that is in use");
        }
        
        roleRepository.deleteById(roleId);
    }

    // Traditional user creation with encoded password
    public User createTraditionalUser(String username, String password, String email, String fullName, String phone) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }
        
        // Encode password before saving
        String encodedPassword = passwordEncoder.encode(password);
        
        User newUser = new User(username, encodedPassword, null, email, fullName, phone);
        // Set default role as USER
        Role userRole = roleRepository.findByName("USER")
            .orElseThrow(() -> new RuntimeException("Default USER role not found"));
        newUser.setRole(userRole);
        return userRepository.save(newUser);
    }

    // Verify password for traditional login
    public boolean verifyPassword(String username, String rawPassword) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return passwordEncoder.matches(rawPassword, user.get().getPassword());
        }
        return false;
    }
}
