package com.task.TeamManager.service;

import com.task.TeamManager.dto.DashboardStatsDTO;
import com.task.TeamManager.dto.ProjectDTO;
import com.task.TeamManager.dto.TaskDTO;
import com.task.TeamManager.model.TaskStatus;
import com.task.TeamManager.model.Tasks;
import com.task.TeamManager.model.Projects;
import com.task.TeamManager.repository.ProjectsRepository;
import com.task.TeamManager.repository.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired
    private ProjectsRepository projectsRepository;
    @Autowired
    private TasksRepository tasksRepository;

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setActiveProjects(projectsRepository.countByEndDateIsNullOrEndDateAfter(java.time.LocalDateTime.now()));
        // Tasks due = tasks with due date today or earlier and not completed
        long tasksDue = tasksRepository.countByDueDateLessThanEqualAndStatusNot(
            java.time.LocalDateTime.now(), com.task.TeamManager.model.TaskStatus.DONE);
        stats.setTasksDue(tasksDue);
        stats.setCompletedTasks(tasksRepository.countByStatus(com.task.TeamManager.model.TaskStatus.DONE));
        stats.setInProgressTasks(tasksRepository.countByStatus(com.task.TeamManager.model.TaskStatus.IN_PROGRESS));
        return stats;
    }

    public Page<ProjectDTO> getRecentProjects(Pageable pageable) {
        return projectsRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(ProjectDTO::fromEntity);
    }

    public Page<TaskDTO> getRecentTasks(Pageable pageable) {
        return tasksRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(TaskDTO::fromEntity);
    }
} 