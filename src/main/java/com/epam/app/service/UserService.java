package com.epam.app.service;

import com.epam.app.model.Role;
import com.epam.app.model.User;

/**
 * User service interface.
 */
public interface UserService extends RudService<User> {
    /**
     * Adds user with certain role.
     * @param user new user.
     * @param role user's role.
     * @return user with set id
     * if added successfully.
     */
    User add(User user, Role role);
}
