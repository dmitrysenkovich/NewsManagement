package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Role repository interface.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
}
