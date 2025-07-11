package com.task.TeamManager.controller;

import com.task.TeamManager.dto.ProjectDTO;
import com.task.TeamManager.model.Projects;
import com.task.TeamManager.model.Users;
import com.task.TeamManager.service.ProjectService;
import com.task.TeamManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER')")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO dto) {
        // Validate required fields
        if (dto.name == null || dto.name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Project name is required.");
        }
        if (dto.projectManagerId == null) {
            return ResponseEntity.badRequest().body("Project manager ID is required.");
        }

        // Validate project manager exists and has the correct role
        Optional<Users> pmOpt = userService.findUserById(dto.projectManagerId);
        if (pmOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project manager not found with ID: " + dto.projectManagerId);
        }
        Users pm = pmOpt.get();
        boolean isManager = pm.getRoles().stream()
            .anyMatch(role -> role.getName().name().equals("ROLE_PROJECT_MANAGER"));
        if (!isManager) {
            return ResponseEntity.badRequest().body("User with ID " + dto.projectManagerId + " is not a project manager.");
        }

        try {
            Projects project = new Projects();
            project.setName(dto.name);
            // Convert LocalDate to LocalDateTime (start of day for start date, end of day for end date)
            if (dto.startDate != null) {
                project.setStartDate(dto.startDate.atStartOfDay());
            }
            if (dto.endDate != null) {
                project.setEndDate(dto.endDate.atTime(23, 59, 59));
            }

            Projects saved = projectService.createProject(project, dto.projectManagerId);
            return ResponseEntity.status(201).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating project: " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER') or hasRole('ROLE_TEAM_MEMBER')")
    public ResponseEntity<Page<Projects>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(projectService.getAllProjects(PageRequest.of(page, size)));
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint working!");
    }

        @GetMapping("/by-manager/{managerId}")
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER') or hasRole('ROLE_TEAM_MEMBER')")
    public ResponseEntity<?> getProjectsByManager(@PathVariable Long managerId) {
        System.out.println("=== getProjectsByManager called with managerId: " + managerId + " ===");
        try {
            // Validate that the manager exists
            Optional<Users> managerOpt = userService.findUserById(managerId);
            if (managerOpt.isEmpty()) {
                System.out.println("Manager not found with ID: " + managerId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project manager not found with ID: " + managerId);
            }
            
            Users manager = managerOpt.get();
            System.out.println("Found manager: " + manager.getUsername());
            boolean isManager = manager.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("ROLE_PROJECT_MANAGER"));
            if (!isManager) {
                System.out.println("User is not a project manager: " + manager.getUsername());
                return ResponseEntity.badRequest().body("User with ID " + managerId + " is not a project manager.");
            }
            
            System.out.println("Getting projects for manager: " + manager.getUsername());
            List<Projects> projects = projectService.getProjectsByManager(manager);
            System.out.println("Found " + projects.size() + " projects");
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            System.out.println("Error in getProjectsByManager: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching projects: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER') or hasRole('ROLE_TEAM_MEMBER')")
    public ResponseEntity<Projects> getProjectById(@PathVariable Long id) {
        Optional<Projects> project = projectService.getProjectById(id);
        return ResponseEntity.of(project);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER')")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody ProjectDTO dto) {
        // Validate required fields
        if (dto.name == null || dto.name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Project name is required.");
        }

        // Validate project manager if provided
        if (dto.projectManagerId != null) {
            Optional<Users> pmOpt = userService.findUserById(dto.projectManagerId);
            if (pmOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project manager not found with ID: " + dto.projectManagerId);
            }
            Users pm = pmOpt.get();
            boolean isManager = pm.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("ROLE_PROJECT_MANAGER"));
            if (!isManager) {
                return ResponseEntity.badRequest().body("User with ID " + dto.projectManagerId + " is not a project manager.");
            }
        }

        try {
            Projects updated = new Projects();
            updated.setName(dto.name);
            // Convert LocalDate to LocalDateTime (start of day for start date, end of day for end date)
            if (dto.startDate != null) {
                updated.setStartDate(dto.startDate.atStartOfDay());
            }
            if (dto.endDate != null) {
                updated.setEndDate(dto.endDate.atTime(23, 59, 59));
            }
            
            // Validate dates if both are provided
            if (updated.getStartDate() != null && updated.getEndDate() != null) {
                if (updated.getStartDate().isAfter(updated.getEndDate())) {
                    return ResponseEntity.badRequest().body("Start date cannot be after end date.");
                }
            }
            
            Projects project = projectService.updateProject(id, updated, dto.projectManagerId);
            return ResponseEntity.ok(project);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating project: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER')")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.ok("Project deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting project: " + e.getMessage());
        }
    }
}
