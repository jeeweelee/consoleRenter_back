package com.jia.consolerenter.repository;

import com.jia.consolerenter.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String roleName);
    boolean existsByName(String roleName); // Change this to accept a String, not Role
}