package com.task.TeamManager.repository;

import com.task.TeamManager.model.TaskStatus;
import com.task.TeamManager.model.Tasks;
import com.task.TeamManager.model.Users;
import com.task.TeamManager.model.Projects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {
    // 1. Find tasks by assigned user, with pagination
    Page<Tasks> findByAssignedTo(Users assignedTo, Pageable pageable);

    // 2. Find tasks by project, with pagination
    Page<Tasks> findByProject(Projects project, Pageable pageable);
    Page<Tasks> findByStatus(TaskStatus status, Pageable pageable);
    // 3. Find tasks by assigned user and status, with pagination
    Page<Tasks> findByAssignedToAndStatus(Users assignedTo, TaskStatus status, Pageable pageable);
    Page<Tasks> findByProjectAndStatus(Projects project, TaskStatus status, Pageable pageable);
    // 4. Find tasks by due date before or on a specific date
    Page<Tasks> findByDueDateLessThanEqual(LocalDateTime dueDate, Pageable pageable);

    // 5. Get all tasks with pagination
    Page<Tasks> findAll(Pageable pageable);

    // 6. Count tasks by status
    long countByStatus(TaskStatus status);

    // 7. Count tasks due before or on a specific date
    long countByDueDateLessThanEqual(LocalDateTime dueDate);

    // 8. Find all tasks ordered by creation date (recent tasks first)
    Page<Tasks> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
