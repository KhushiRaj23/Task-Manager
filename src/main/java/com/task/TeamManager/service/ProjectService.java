package com.task.TeamManager.service;

import com.task.TeamManager.model.Projects;
import com.task.TeamManager.model.Users;
import com.task.TeamManager.repository.ProjectsRepository;
import com.task.TeamManager.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectsRepository projectsRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Projects createProject(Projects project, Long projectManagerId) {
        Users manager = usersRepository.findById(projectManagerId)
                .orElseThrow(() -> new IllegalArgumentException("Project manager not found with ID: " + projectManagerId));
        project.setProjectManagerId(manager);
        return projectsRepository.save(project);
    }

    public Page<Projects> getAllProjects(Pageable pageable) {
        return projectsRepository.findAll(pageable);
    }

    public Optional<Projects> getProjectById(Long id) {
        return projectsRepository.findById(id);
    }

    public List<Projects> getProjectsByManager(Users manager) {
        return projectsRepository.findByProjectManagerId(manager);
    }

    public void deleteProject(Long id) {
        if (!projectsRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found with ID: " + id);
        }
        projectsRepository.deleteById(id);
    }

    public Projects updateProject(Long projectId, Projects updatedProject, Long managerId) {
        Projects existing = projectsRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));

        existing.setName(updatedProject.getName());
        existing.setStartDate(updatedProject.getStartDate());
        existing.setEndDate(updatedProject.getEndDate());

        if (managerId != null) {
            Users manager = usersRepository.findById(managerId)
                    .orElseThrow(() -> new IllegalArgumentException("Project manager not found with ID: " + managerId));
            existing.setProjectManagerId(manager);
        }

        return projectsRepository.save(existing);
    }
}
