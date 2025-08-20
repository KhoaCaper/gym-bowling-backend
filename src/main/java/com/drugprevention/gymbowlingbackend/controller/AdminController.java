package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.AuthDTO;
import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.entity.Role;
import com.drugprevention.gymbowlingbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Admin Controller
 * Handles administrative operations for user and role management
 * 
 * @author GymBo Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "Administrative endpoints for user and role management")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users
     * 
     * @return List of all users
     */
    @GetMapping("/users")
    @Operation(
        summary = "Get all users", 
        description = "Retrieve all users in the system (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved users",
            content = @Content(schema = @Schema(implementation = User.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Access denied - Admin role required"
        )
    })
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            
            // Create clean user data to avoid circular references
            List<Map<String, Object>> cleanUsers = users.stream()
                .map(user -> Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "fullName", user.getFullName(),
                    "phone", user.getPhone() != null ? user.getPhone() : "",
                    "role", Map.of(
                        "id", user.getRole().getId(),
                        "name", user.getRole().getName(),
                        "description", user.getRole().getDescription() != null ? user.getRole().getDescription() : ""
                    ),
                    "isActive", user.getIsActive(),
                    "createdAt", user.getCreatedAt(),
                    "updatedAt", user.getUpdatedAt() != null ? user.getUpdatedAt() : user.getCreatedAt()
                ))
                .toList();
            
            return ResponseEntity.ok(Map.of(
                "users", cleanUsers,
                "count", cleanUsers.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to retrieve users: " + e.getMessage()));
        }
    }

    /**
     * Get user by ID
     * 
     * @param id User ID
     * @return User information
     */
    @GetMapping("/users/{id}")
    @Operation(
        summary = "Get user by ID", 
        description = "Retrieve specific user by ID (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved user",
            content = @Content(schema = @Schema(implementation = User.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        try {
            var user = userService.getUserById(id);
            if (user.isPresent()) {
                User userEntity = user.get();
                // Create clean user data to avoid circular references
                Map<String, Object> cleanUser = Map.of(
                    "id", userEntity.getId(),
                    "username", userEntity.getUsername(),
                    "email", userEntity.getEmail(),
                    "fullName", userEntity.getFullName(),
                    "phone", userEntity.getPhone() != null ? userEntity.getPhone() : "",
                    "role", Map.of(
                        "id", userEntity.getRole().getId(),
                        "name", userEntity.getRole().getName(),
                        "description", userEntity.getRole().getDescription() != null ? userEntity.getRole().getDescription() : ""
                    ),
                    "isActive", userEntity.getIsActive(),
                    "createdAt", userEntity.getCreatedAt(),
                    "updatedAt", userEntity.getUpdatedAt() != null ? userEntity.getUpdatedAt() : userEntity.getCreatedAt()
                );
                return ResponseEntity.ok(Map.of("user", cleanUser));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to retrieve user: " + e.getMessage()));
        }
    }

    /**
     * Update user role
     * 
     * @param id User ID
     * @param request Role update request
     * @return Updated user information
     */
    @PutMapping("/users/{id}/role")
    @Operation(
        summary = "Update user role", 
        description = "Change user role (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User role updated successfully",
            content = @Content(schema = @Schema(implementation = User.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid role or user not found",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, Object>> updateUserRole(
            @PathVariable Long id, 
            @RequestBody Map<String, String> request) {
        try {
            String roleName = request.get("role");
            if (roleName == null || roleName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Role name is required"));
            }

            User updatedUser = userService.updateUserRole(id, roleName);
            // Create clean user data to avoid circular references
            Map<String, Object> cleanUser = Map.of(
                "id", updatedUser.getId(),
                "username", updatedUser.getUsername(),
                "email", updatedUser.getEmail(),
                "fullName", updatedUser.getFullName(),
                "phone", updatedUser.getPhone() != null ? updatedUser.getPhone() : "",
                "role", Map.of(
                    "id", updatedUser.getRole().getId(),
                    "name", updatedUser.getRole().getName(),
                    "description", updatedUser.getRole().getDescription() != null ? updatedUser.getRole().getDescription() : ""
                ),
                "isActive", updatedUser.getIsActive(),
                "createdAt", updatedUser.getCreatedAt(),
                "updatedAt", updatedUser.getUpdatedAt() != null ? updatedUser.getUpdatedAt() : updatedUser.getCreatedAt()
            );
            return ResponseEntity.ok(Map.of(
                "message", "User role updated successfully",
                "user", cleanUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update user role: " + e.getMessage()));
        }
    }

    /**
     * Toggle user status (active/inactive)
     * 
     * @param id User ID
     * @return Updated user information
     */
    @PatchMapping("/users/{id}/toggle-status")
    @Operation(
        summary = "Toggle user status", 
        description = "Activate or deactivate user account (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User status updated successfully",
            content = @Content(schema = @Schema(implementation = User.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "User not found",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, Object>> toggleUserStatus(@PathVariable Long id) {
        try {
            User updatedUser = userService.toggleUserStatus(id);
            // Create clean user data to avoid circular references
            Map<String, Object> cleanUser = Map.of(
                "id", updatedUser.getId(),
                "username", updatedUser.getUsername(),
                "email", updatedUser.getEmail(),
                "fullName", updatedUser.getFullName(),
                "phone", updatedUser.getPhone() != null ? updatedUser.getPhone() : "",
                "role", Map.of(
                    "id", updatedUser.getRole().getId(),
                    "name", updatedUser.getRole().getName(),
                    "description", updatedUser.getRole().getDescription() != null ? updatedUser.getRole().getDescription() : ""
                ),
                "isActive", updatedUser.getIsActive(),
                "createdAt", updatedUser.getCreatedAt(),
                "updatedAt", updatedUser.getUpdatedAt() != null ? updatedUser.getUpdatedAt() : updatedUser.getCreatedAt()
            );
            return ResponseEntity.ok(Map.of(
                "message", "User status updated successfully",
                "user", cleanUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update user status: " + e.getMessage()));
        }
    }

    /**
     * Delete user
     * 
     * @param id User ID
     * @return Success message
     */
    @DeleteMapping("/users/{id}")
    @Operation(
        summary = "Delete user", 
        description = "Delete user account (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User deleted successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "User not found or cannot be deleted",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete user: " + e.getMessage()));
        }
    }

    /**
     * Get all roles
     * 
     * @return List of all roles
     */
    @GetMapping("/roles")
    @Operation(
        summary = "Get all roles", 
        description = "Retrieve all available roles (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved roles",
            content = @Content(schema = @Schema(implementation = Role.class))
        )
    })
    public ResponseEntity<Map<String, Object>> getAllRoles() {
        try {
            List<Role> roles = userService.getAllRoles();
            return ResponseEntity.ok(Map.of(
                "roles", roles,
                "count", roles.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to retrieve roles: " + e.getMessage()));
        }
    }

    /**
     * Create new role
     * 
     * @param request Role creation request
     * @return Created role
     */
    @PostMapping("/roles")
    @Operation(
        summary = "Create new role", 
        description = "Create a new role (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Role created successfully",
            content = @Content(schema = @Schema(implementation = Role.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Role already exists or validation failed",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, Object>> createRole(@RequestBody Map<String, String> request) {
        try {
            String roleName = request.get("name");
            if (roleName == null || roleName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Role name is required"));
            }

            Role newRole = userService.createRole(roleName);
            return ResponseEntity.ok(Map.of(
                "message", "Role created successfully",
                "role", newRole
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create role: " + e.getMessage()));
        }
    }

    /**
     * Delete role
     * 
     * @param id Role ID
     * @return Success message
     */
    @DeleteMapping("/roles/{id}")
    @Operation(
        summary = "Delete role", 
        description = "Delete a role (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Role deleted successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Role not found or cannot be deleted",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, String>> deleteRole(@PathVariable Long id) {
        try {
            userService.deleteRole(id);
            return ResponseEntity.ok(Map.of("message", "Role deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete role: " + e.getMessage()));
        }
    }
}
