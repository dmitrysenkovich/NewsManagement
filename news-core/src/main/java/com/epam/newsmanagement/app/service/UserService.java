package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Role;
import com.epam.newsmanagement.app.model.User;

/**
 * User service interface.
 */
public interface UserService extends CrudService<User> {
    /**
     * Adds user with certain role.
     * @param user new user.
     * @param role user's role.
     * @return user with set id
     * if added successfully.
     * @throws ServiceException
     */
    User add(User user, Role role) throws ServiceException;
}
