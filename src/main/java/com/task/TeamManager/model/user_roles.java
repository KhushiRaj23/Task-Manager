package com.task.TeamManager.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_roles")
@IdClass(UserRoleId.class)
public class user_roles implements Serializable{
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "role_id")
    private Long roleId;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserRoleId implements Serializable {
    private Long userId;
    private Long roleId;
}
