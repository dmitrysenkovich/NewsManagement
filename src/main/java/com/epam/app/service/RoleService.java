package com.epam.app.service;

import com.epam.app.model.Role;

/**
 * Role service interface.
 */
public interface RoleService extends RudService<Role> {
    /**
     * Adds new role.
     * @param role new role.
     * @return this new role with id set
     * if added successfully.
     */
    Role add(Role role);
}
