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

    /**
     * Find projects assigned to a specific project manager.
     */
    List<Projects> findByProjectManagerId(Users projectManager);

    /**
     * Get all projects with pagination.
     */
    Page<Projects> findAll(Pageable pageable);

    /**
     * Search projects by name (case-insensitive).
     */
    List<Projects> findByNameContainingIgnoreCase(String name);

    /**
     * Count active projects where the end date is either null or in the future.
     */
     @Query("SELECT COUNT(p) FROM projects p WHERE p.active = true")
     long countActiveProjects();

    /**
     * Count projects with endDate null or after the given date.
     */
    long countByEndDateIsNullOrEndDateAfter(java.time.LocalDateTime date);

    /**
     * Get all projects ordered by creation timestamp in descending order.
     */
    List<Projects> findAllByOrderByCreatedAtDesc();

    // Paginated recent projects
    org.springframework.data.domain.Page<Projects> findAllByOrderByCreatedAtDesc(org.springframework.data.domain.Pageable pageable);
}
