package com.task.TeamManager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class Users{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "username is required")
    @Column(nullable = false, unique = true)
    @Size(min = 5,max = 50,message = "Username must be between 5 to 50 character")
    private String username;

    @NonNull
    @NotBlank(message = "Email is required")
    @Email(message = "Enter valid email")
    @Column(nullable = false, unique = true)
    @Size(max = 100,message = "Email must be under 100 character")
    private String email;

    @NonNull
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    @Size(min =5,max =120,message ="Password must be between 5 to 50 character")
    private String password;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<Roles> roles = new HashSet<>();

    @OneToMany(mappedBy = "projectManagerId", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Projects> managedProjects = new HashSet<>();

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Tasks> assignedTasks = new HashSet<>();

}
