package com.task.TeamManager.service;

import com.task.TeamManager.model.*;
import com.task.TeamManager.repository.ProjectsRepository;
import com.task.TeamManager.repository.TasksRepository;
import com.task.TeamManager.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TaskService {
    private final TasksRepository tasksRepository;
    private final ProjectsRepository projectsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public TaskService(TasksRepository tasksRepository,
                       ProjectsRepository projectsRepository,
                       UsersRepository usersRepository) {
        this.tasksRepository = tasksRepository;
        this.projectsRepository = projectsRepository;
        this.usersRepository = usersRepository;
    }
    public boolean validateTaskTitle(String title){
        return title!=null && !title.trim().isEmpty();
    }
    public boolean validateTaskDueDate(LocalDateTime dueDate){
        return dueDate!=null && !dueDate.isBefore(LocalDateTime.now());
    }

    public boolean assignProjectToTask(Long projectId, Tasks task){
        Optional<Projects> projects=projectsRepository.findById(projectId);
        if(projects.isPresent()){
            task.setProject(projects.get());
            return true;
        }
        return false;
    }
    public  boolean assignUserToTask(Long userId,Tasks task){
        Optional<Users> user=usersRepository.findById(userId);
        if(user.isPresent()){
            task.setAssignedTo(user.get());
            return true;
        }
        return false;
    }

   public Tasks createTask(Tasks tasks,Long projectId,Long assignedUserId){
        if(tasks.getPriority()==null){
            tasks.setPriority(TaskPriority.MEDIUM);
        }
        if(tasks.getStatus()==null){
            tasks.setStatus(TaskStatus.TODO);
        }
        Projects projects=projectsRepository.findById(projectId).orElseThrow(()->new IllegalArgumentException("Project Id not found: "+projectId));
        tasks.setProject(projects);

        if(assignedUserId!=null){
            Users user = usersRepository.findById(assignedUserId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + assignedUserId));
            tasks.setAssignedTo(user);

        }
        return tasksRepository.save(tasks);
   }

   public Optional<Tasks> findTaskById(Long id){
        return tasksRepository.findById(id);
   }
   public Page<Tasks> getAllTasks(Pageable pageable){
        return tasksRepository.findAll(pageable);
   }
   public Page<Tasks> getTaskByAssignedUser(Long userId,Pageable pageable){
        Users user=usersRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("User not found with id: "+userId));
        return tasksRepository.findByAssignedTo(user,pageable);

   }

   public Page<Tasks> getTaskByProjectAndStatus(Long projectId,TaskStatus status,Pageable pageable){
        Projects projects=projectsRepository.findById(projectId).orElseThrow(()->new IllegalArgumentException("Project not found with id: "+projectId));
        return tasksRepository.findByProjectAndStatus(projects,status,pageable);
   }

    public Page<Tasks> getTaskByProject(Long projectId, Pageable pageable) {
        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));
        return tasksRepository.findByProject(project, pageable);
    }
    public Page<Tasks> getTaskByStatus(TaskStatus status,Pageable pageable){
        return tasksRepository.findByStatus(status,pageable);
    }

    public Page<Tasks> getTasksByAssignedUserAndStatus(Long userId, TaskStatus status, Pageable pageable) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return tasksRepository.findByAssignedToAndStatus(user, status, pageable);
    }

    public void deleteTask(Long taskId) {
        if (!tasksRepository.existsById(taskId)) {
            throw new IllegalArgumentException("Task not found with ID: " + taskId);
        }
        tasksRepository.deleteById(taskId);
    }

    public Tasks updateTask(Long taskId, Tasks updatedTask, Long projectId, Long assignedUserId) {
        Tasks existingTask = tasksRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setStatus(updatedTask.getStatus());

        if(updatedTask.getDueDate()!=null){
            existingTask.setDueDate(updatedTask.getDueDate());
            if(updatedTask.getDueDate().isBefore(LocalDateTime.now()) &&
                    updatedTask.getStatus()!=TaskStatus.DONE){
                System.out.println("Warning: Updating with past due date and task is not DONE.");
            }
        }
        if(projectId!=null ){

            Projects project = projectsRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));existingTask.setProject(project);
            existingTask.setProject(project);
        }
        if(assignedUserId!=null){
            Users user = usersRepository.findById(assignedUserId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + assignedUserId));
            existingTask.setAssignedTo(user);
        }
        else if(updatedTask.getAssignedTo()!=null && updatedTask.getAssignedTo().getId()!=null){
            // Keep the existing assigned user
        }
        else {
            // Clear the assigned user if no valid assignment
            existingTask.setAssignedTo(null);
        }
        return tasksRepository.save(existingTask);
    }

    //Update only the status of a task
    public  Tasks updateTaskStatus(Long taskId,TaskStatus status){
        Tasks task=tasksRepository.findById(taskId).orElseThrow(()->new IllegalArgumentException("Task not found by Id: "+taskId));
        task.setStatus(status);
        return tasksRepository.save(task);

    }

}
