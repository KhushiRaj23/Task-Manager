package com.task.TeamManager.dto;

import com.task.TeamManager.model.Users;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDTO{
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;

    public UserDTO(Users user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream()
                .map(role -> role.getName().name()) // Convert enum to string
                .collect(Collectors.toSet());
    }

    // Getters & Setters (or use Lombok if preferred)
}
