package com.task.TeamManager.repository;

import com.task.TeamManager.model.Projects;
import com.task.TeamManager.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectsRepository extends JpaRepository<Projects, Long> {
    //finding projects by project manager
    List<Projects> findByProjectManager(Users projectManager);

    //getting all projects with pagination
    Page<Projects> findAll(Pageable pageable);

    //finding projects by name containing a string
    List<Projects> findByNameContainingIgnoreCase(String name);

    //counting active projects (not ended or end date is in the future)
    @Query("SELECT COUNT(p) FROM projects p WHERE p.endDate IS NULL OR p.endDate > CURRENT_DATE")
    long countActiveProjects();

    //counting by end date null or end date after a given date
    long countByEndDateIsNullOrEndDateAfter(LocalDate date);

    //finding all projects ordered by creation date
    List<Projects> findAllByOrderByCreatedAtDesc();
}
