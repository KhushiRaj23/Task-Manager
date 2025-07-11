package com.task.TeamManager.service;

import com.task.TeamManager.dto.UserDTO;
import com.task.TeamManager.model.Role;
import com.task.TeamManager.model.Roles;
import com.task.TeamManager.model.Users;
import com.task.TeamManager.repository.RolesRepository;
import com.task.TeamManager.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UsersRepository usersRepository,
                       RolesRepository rolesRepository,
                       PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }


public Users createProjectManagerUser(String username, String email, String password) {
    Users user = new Users();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password)); // Make sure this is already encoded!

    // Fetch the role from the database
    Roles pmRole = rolesRepository.findByName(Role.ROLE_PROJECT_MANAGER)
        .orElseThrow(() -> new RuntimeException("Role not found"));

    user.getRoles().add(pmRole);

    return usersRepository.save(user);
}

    // ✅ Check username availability
    public void checkUsernameAvailability(String username) {
        if (usersRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Error: Username is already taken!");
        }
    }

    // ✅ Register a new user
    public Users registerUser(String username, String email, String rawPassword, Set<String> strRoles) {
        // Validate
        if (usersRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Error: Username is already taken!");
        }
        if (usersRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        // Create user
        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));

        // Assign roles safely
        Set<Roles> fetchedRoles = prepareNewRoles(strRoles);
        user.setRoles(new HashSet<>(fetchedRoles)); // Defensive copy

        return usersRepository.save(user);
    }

    // ✅ Find user by username
    public Optional<Users> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public List<UserDTO> findAllUserDTOs() {
        List<Users> users = usersRepository.findAll();

        List<UserDTO> userDTOs = new ArrayList<>();
        for (Users user : users) {
            userDTOs.add(new UserDTO(user));
        }
        return userDTOs;
    }

    // ✅ Delete user
    public void deleteUser(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        usersRepository.deleteById(id);
    }

    // ✅ Find user by ID
    public Optional<Users> findUserById(Long id) {
        return usersRepository.findById(id);
    }

    // ✅ Assign new roles to a user
    public Users updateUserRoles(Long userId, Set<String> newRoles) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Set<Roles> roles = prepareNewRoles(newRoles);
        user.setRoles(new HashSet<>(roles));

        return usersRepository.save(user);
    }

    // ✅ Assign a specific role to an existing user (additive, does not overwrite)
    public Users addRoleToUser(Long userId, String roleString) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Role roleEnum;
        if (roleString.equalsIgnoreCase("manager")) {
            roleEnum = Role.ROLE_PROJECT_MANAGER;
        } else if (roleString.equalsIgnoreCase("member")) {
            roleEnum = Role.ROLE_TEAM_MEMBER;
        } else if (roleString.equalsIgnoreCase("ROLE_PROJECT_MANAGER")) {
            roleEnum = Role.ROLE_PROJECT_MANAGER;
        } else if (roleString.equalsIgnoreCase("ROLE_TEAM_MEMBER")) {
            roleEnum = Role.ROLE_TEAM_MEMBER;
        } else {
            throw new IllegalArgumentException("Role '" + roleString + "' is invalid. Use 'manager', 'member', 'ROLE_PROJECT_MANAGER', or 'ROLE_TEAM_MEMBER'.");
        }

        Roles roleEntity = rolesRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Error: Role '" + roleEnum + "' not found"));
        user.getRoles().add(roleEntity);
        return usersRepository.save(user);
    }

    // ✅ Assign a role to a user by username (convenience method)
    public Users addRoleToUserByUsername(String username, String roleString) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return addRoleToUser(user.getId(), roleString);
    }

    // ✅ Helper method: Convert string roles to entity roles
    public Set<Roles> prepareNewRoles(Set<String> roleStrings) {
        Set<Roles> roles = new HashSet<>();
        if (roleStrings == null || roleStrings.isEmpty()) {
            Roles defaultRole = rolesRepository.findByName(Role.ROLE_TEAM_MEMBER)
                    .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_TEAM_MEMBER' not found"));
            roles.add(defaultRole);
        } else {
            for (String roleName : roleStrings) {
                Role roleEnum;
                if (roleName.equalsIgnoreCase("manager")) {
                    roleEnum = Role.ROLE_PROJECT_MANAGER;
                } else if (roleName.equalsIgnoreCase("member")) {
                    roleEnum = Role.ROLE_TEAM_MEMBER;
                } else {
                    throw new IllegalArgumentException("Role '" + roleName + "' is invalid. Use 'manager' or 'member'.");
                }

                Roles roleEntity = rolesRepository.findByName(roleEnum)
                        .orElseThrow(() -> new RuntimeException("Error: Role '" + roleEnum + "' not found"));
                roles.add(roleEntity);
            }
        }
        return roles;
    }
}
