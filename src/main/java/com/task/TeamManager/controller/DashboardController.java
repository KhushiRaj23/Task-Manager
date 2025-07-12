package com.task.TeamManager.controller;

import com.task.TeamManager.dto.DashboardStatsDTO;
import com.task.TeamManager.dto.ProjectDTO;
import com.task.TeamManager.dto.TaskDTO;
import com.task.TeamManager.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {
    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

    @GetMapping("/recent-projects")
    public ResponseEntity<Page<ProjectDTO>> getRecentProjects(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(dashboardService.getRecentProjects(PageRequest.of(page, size)));
    }

    @GetMapping("/recent-tasks")
    public ResponseEntity<Page<TaskDTO>> getRecentTasks(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(dashboardService.getRecentTasks(PageRequest.of(page, size)));
    }
} 