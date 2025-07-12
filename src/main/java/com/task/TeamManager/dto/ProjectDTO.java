package com.task.TeamManager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectDTO {
    public String name;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate endDate;
    
    public Long projectManagerId;

    public static ProjectDTO fromEntity(com.task.TeamManager.model.Projects project) {
        ProjectDTO dto = new ProjectDTO();
        dto.name = project.getName();
        if (project.getStartDate() != null) dto.startDate = project.getStartDate().toLocalDate();
        if (project.getEndDate() != null) dto.endDate = project.getEndDate().toLocalDate();
        if (project.getProjectManagerId() != null) dto.projectManagerId = project.getProjectManagerId().getId();
        return dto;
    }
}
