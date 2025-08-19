package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.entity.Role;
import com.drugprevention.gymbowlingbackend.repository.UserRepository;
import com.drugprevention.gymbowlingbackend.repository.RoleRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

import java.util.List;
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
            if (firebaseAuth == null) {
                throw new RuntimeException("Firebase not initialized");
            }
            
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            
            return createOrUpdateUser(firebaseUid, email, name, null);
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
        
        // Since User entity doesn't have isActive field, we'll just return the user
        // You can add isActive field to User entity if needed
        return user;
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
}
