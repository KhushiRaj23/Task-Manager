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
}
