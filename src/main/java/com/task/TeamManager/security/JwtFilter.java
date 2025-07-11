package com.task.TeamManager.security;

import com.task.TeamManager.service.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetail userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            log.info("Processing request: {} {}", request.getMethod(), request.getRequestURI());
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                log.info("JWT token found, extracting username...");
                String username = jwtService.getUsernameFromToken(jwt);
                log.info("Username from token: {}", username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    log.info("Loading user details for username: {}", username);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    log.info("User authorities: {}", userDetails.getAuthorities());
                    
                    if (jwtService.validateJwtToken(jwt)) {
                        log.info("JWT token is valid, setting authentication");
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        log.info("Authentication set successfully");
                    } else {
                        log.warn("JWT token validation failed");
                    }
                } else {
                    log.info("Username is null or authentication already exists");
                }
            } else {
                log.info("No Authorization header found");
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT Filter error: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + e.getMessage());
        }
    }
}
