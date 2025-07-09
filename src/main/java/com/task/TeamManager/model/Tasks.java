package com.task.TeamManager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tasks")
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be under 100 characters")
    @Column(nullable = false)
    private String title;

    @NonNull
    @NotBlank(message = "Description is required")
    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status=TaskStatus.TODO;


    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskPriority priority=TaskPriority.MEDIUM;


    @FutureOrPresent(message = "Due date must be today or in the future")
    @Column(name = "due_date")
    private LocalDateTime dueDate;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Projects project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private Users assignedTo;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}

enum TaskStatus{
    TODO,
    IN_PROGRESS,
    BLOCKED,
    DONE,
    ARCHIVED
}

enum TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}