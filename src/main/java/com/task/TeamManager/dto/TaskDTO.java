package com.task.TeamManager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.task.TeamManager.model.TaskPriority;
import com.task.TeamManager.model.TaskStatus;

import java.time.LocalDate;

public class TaskDTO {
    // DTO for task creation

        public String title;
        public String description;
        public TaskStatus status;
        public TaskPriority taskPriority;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        public LocalDate dueDate;
        
        public Long projectId;
        public Long assignedToId;

    public static TaskDTO fromEntity(com.task.TeamManager.model.Tasks task) {
        TaskDTO dto = new TaskDTO();
        dto.title = task.getTitle();
        dto.description = task.getDescription();
        dto.status = task.getStatus();
        dto.taskPriority = task.getPriority();
        if (task.getDueDate() != null) dto.dueDate = task.getDueDate().toLocalDate();
        if (task.getProject() != null) dto.projectId = task.getProject().getId();
        if (task.getAssignedTo() != null) dto.assignedToId = task.getAssignedTo().getId();
        return dto;
    }
}
