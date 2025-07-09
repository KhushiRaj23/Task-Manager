package com.task.TeamManager.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "projects")
public class Projects {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "project name is required")
    @Size(min = 3,max = 100,message = "project name should be betwwen 3 to 100 characters")
    private String name;

    @PastOrPresent(message = "Start date cannot be in the future")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Future(message = "End date must be in the future")
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "project_manager_id" ,nullable = false)
    private Users projectManager;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<Tasks> tasks=new HashSet<>();

}
