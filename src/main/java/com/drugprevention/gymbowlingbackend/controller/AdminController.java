package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.entity.Role;
import com.drugprevention.gymbowlingbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "APIs for admin to manage users and roles")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users", 
               description = "Get all users in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                content = @Content(schema = @Schema(implementation = User.class)))
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID", 
               description = "Get a specific user by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
            .map(user -> ResponseEntity.ok(user))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}/role")
    @Operation(summary = "Update user role", 
               description = "Update the role of a specific user")
    @ApiResponse(responseCode = "200", description = "User role updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid role or user not found")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestParam String roleName) {
        try {
            User updatedUser = userService.updateUserRole(id, roleName);
            return ResponseEntity.ok(Map.of(
                "message", "User role updated successfully",
                "user", updatedUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update user role: " + e.getMessage()));
        }
    }

    @PatchMapping("/users/{id}/toggle-status")
    @Operation(summary = "Toggle user status", 
               description = "Toggle user active/inactive status")
    @ApiResponse(responseCode = "200", description = "User status updated successfully")
    @ApiResponse(responseCode = "400", description = "User not found")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id) {
        try {
            User updatedUser = userService.toggleUserStatus(id);
            return ResponseEntity.ok(Map.of(
                "message", "User status updated successfully",
                "user", updatedUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update user status: " + e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete user", 
               description = "Delete a user by ID")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "400", description = "User not found")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete user: " + e.getMessage()));
        }
    }

    @GetMapping("/roles")
    @Operation(summary = "Get all roles", 
               description = "Get all available roles in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved roles",
                content = @Content(schema = @Schema(implementation = Role.class)))
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = userService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/roles")
    @Operation(summary = "Create new role", 
               description = "Create a new role")
    @ApiResponse(responseCode = "200", description = "Role created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or role already exists")
    public ResponseEntity<?> createRole(@RequestParam String roleName) {
        try {
            Role createdRole = userService.createRole(roleName);
            return ResponseEntity.ok(Map.of(
                "message", "Role created successfully",
                "role", createdRole
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create role: " + e.getMessage()));
        }
    }

    @DeleteMapping("/roles/{id}")
    @Operation(summary = "Delete role", 
               description = "Delete a role by ID")
    @ApiResponse(responseCode = "200", description = "Role deleted successfully")
    @ApiResponse(responseCode = "400", description = "Role not found or in use")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        try {
            userService.deleteRole(id);
            return ResponseEntity.ok(Map.of("message", "Role deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete role: " + e.getMessage()));
        }
    }
}
