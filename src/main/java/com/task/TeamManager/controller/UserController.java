package com.task.TeamManager.controller;

import com.task.TeamManager.dto.UserDTO;
import com.task.TeamManager.model.Users;
import com.task.TeamManager.repository.UsersRepository;
import com.task.TeamManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UsersRepository usersRepository;
    @Autowired private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDtos = userService.findAllUserDTOs(); // Already DTOs
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return userService.findUserById(id)
                .map(user -> ResponseEntity.ok(new UserDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/roles")
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER')")
    public ResponseEntity<?> updateUserRoles(@PathVariable Long id, @RequestBody Map<String, Set<String>> rolesMap){
        try{
            Set<String> roles=rolesMap.get("roles");
            if(roles==null || roles.isEmpty()){
                return ResponseEntity.badRequest().body("Roles list cannot be empty.");
            }
            userService.updateUserRoles(id,roles);
            return ResponseEntity.ok("User updated successfully");
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + id);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating roles.");

        }
    }

    @PostMapping("/{id}/add-role")
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER')")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String role = body.get("role");
        if (role == null || role.isEmpty()) {
            return ResponseEntity.badRequest().body("Role is required.");
        }
        try {
            Users updated = userService.addRoleToUser(id, role);
            return ResponseEntity.ok(new UserDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding role to user.");
        }
    }

    @PostMapping("/{username}/add-role")
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER')")
    public ResponseEntity<?> addRoleToUserByUsername(@PathVariable String username, @RequestBody Map<String, String> body) {
        String role = body.get("role");
        if (role == null || role.isEmpty()) {
            return ResponseEntity.badRequest().body("Role is required.");
        }
        try {
            Users updated = userService.addRoleToUserByUsername(username, role);
            return ResponseEntity.ok(new UserDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding role to user.");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User Deleted Sucessfully");
    }


}
