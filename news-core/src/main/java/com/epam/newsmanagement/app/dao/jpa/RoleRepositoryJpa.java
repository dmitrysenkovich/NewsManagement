package com.epam.newsmanagement.app.dao.jpa;

import com.epam.newsmanagement.app.dao.RoleRepository;
import com.epam.newsmanagement.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Role repository interface.
 */
public interface RoleRepositoryJpa extends RoleRepository, JpaRepository<Role, Long> {
}
