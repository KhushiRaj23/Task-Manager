package com.task.TeamManager.dto;

import lombok.Data;

@Data
public class DashboardStatsDTO {
    private long activeProjects;
    private long tasksDue;
    private long completedTasks;
    private long inProgressTasks;
} 