package com.task.TeamManager.repository;

import com.task.TeamManager.model.Roles;
import com.task.TeamManager.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(Role name);
}
