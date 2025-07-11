package com.task.TeamManager.controller;

import com.task.TeamManager.dto.TaskDTO;
import com.task.TeamManager.model.TaskPriority;
import com.task.TeamManager.model.TaskStatus;
import com.task.TeamManager.model.Tasks;
import com.task.TeamManager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER')")
    public ResponseEntity<Tasks> createTask(@RequestBody TaskDTO taskDTO) {
        System.out.println("=== createTask called ===");
        Tasks createdTask = new Tasks();
        try {
            Tasks tasks = new Tasks();
            tasks.setTitle(taskDTO.title);
            tasks.setDescription(taskDTO.description);
            tasks.setStatus(taskDTO.status != null ? taskDTO.status : TaskStatus.TODO);
            tasks.setPriority(taskDTO.taskPriority != null ? taskDTO.taskPriority : TaskPriority.MEDIUM);
            
            // Convert LocalDate to LocalDateTime (end of day for due date)
            if (taskDTO.dueDate != null) {
                tasks.setDueDate(taskDTO.dueDate.atTime(23, 59, 59));
            }

            createdTask = taskService.createTask(tasks, taskDTO.projectId, taskDTO.assignedToId);
            System.out.println("Task created successfully with ID: " + createdTask.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            System.out.println("RuntimeException: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.status(201).body(createdTask);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER')")
    public ResponseEntity<Tasks> getTaskById(@PathVariable("id") Long taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task Id must not be null");
        }
        Tasks task = taskService.findTaskById(taskId).orElseThrow(() -> new IllegalArgumentException("Task id not found: " + taskId));
        return ResponseEntity.ok(task);
    }

    // ✅ Get all tasks with pagination
    @GetMapping
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER') or hasRole('ROLE_TEAM_MEMBER')")
    public ResponseEntity<Page<Tasks>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Tasks> tasksPage = taskService.getAllTasks(pageable);
        return ResponseEntity.ok(tasksPage);
    }

    // ✅ Get tasks by assigned user ID
    @GetMapping("/assigned/{userId}")
    public ResponseEntity<Page<Tasks>> getTasksByAssignedUser(@PathVariable Long userId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getTaskByAssignedUser(userId, pageable));
    }

    // ✅ Get tasks by project and status
    @GetMapping("/project/{projectId}/status")
    public ResponseEntity<Page<Tasks>> getTasksByProjectAndStatus(@PathVariable Long projectId,
                                                                  @RequestParam TaskStatus status,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getTaskByProjectAndStatus(projectId, status, pageable));
    }

    // ✅ Get tasks by status
    @GetMapping("/status")
    public ResponseEntity<Page<Tasks>> getTasksByStatus(@RequestParam TaskStatus status,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getTaskByStatus(status, pageable));
    }

    // ✅ Get tasks by assigned user and status
    @GetMapping("/assigned/{userId}/status")
    public ResponseEntity<Page<Tasks>> getTasksByAssignedUserAndStatus(@PathVariable Long userId,
                                                                       @RequestParam TaskStatus status,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getTasksByAssignedUserAndStatus(userId, status, pageable));
    }

    // ✅ Delete task by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROJECT_MANAGER')")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }

    // ✅ Update only task status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Tasks> updateTaskStatus(@PathVariable Long id,
                                                  @RequestParam TaskStatus status) {
        Tasks updatedTask = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(updatedTask);
    }

    // ✅ Update full task
    @PutMapping("/{id}")
    public ResponseEntity<Tasks> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        Tasks task = new Tasks();
        task.setTitle(taskDTO.title);
        task.setDescription(taskDTO.description);
        task.setPriority(taskDTO.taskPriority);
        task.setStatus(taskDTO.status);
        
        // Convert LocalDate to LocalDateTime (end of day for due date)
        if (taskDTO.dueDate != null) {
            task.setDueDate(taskDTO.dueDate.atTime(23, 59, 59));
        }

        Tasks updated = taskService.updateTask(id, task, taskDTO.projectId, taskDTO.assignedToId);
        return ResponseEntity.ok(updated);
    }
}
