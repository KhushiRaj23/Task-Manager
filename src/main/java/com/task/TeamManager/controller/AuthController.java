package com.task.TeamManager.controller;

import com.task.TeamManager.model.Users;
import com.task.TeamManager.service.JwtService;
import com.task.TeamManager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import com.task.TeamManager.security.UserDetail;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserDetail userDetailsService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> loginData) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginData.get("username"), loginData.get("password"))
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginData.get("username"));
            Set<String> roles = new HashSet<>();
            userDetails.getAuthorities().forEach(a -> roles.add(a.getAuthority()));
            String jwt = jwtService.generateJwtToken(userDetails.getUsername(), roles);
            return ResponseEntity.ok(Map.of("token", jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> data) {
        try {
            String username = data.get("username").toString();
            String email = data.get("email").toString();
            String password = data.get("password").toString();

            Set<String> roles = new HashSet<>();
            if (data.get("roles") != null) {
                roles = new HashSet<>((List<String>) data.get("roles"));
            }

            Users user = userService.registerUser(username, email, password, roles);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            e.printStackTrace(); // 👈 Add this
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
