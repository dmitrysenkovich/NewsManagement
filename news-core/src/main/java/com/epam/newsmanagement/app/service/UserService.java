package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.exception.DaoException;
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
    default User add(User user, Role role) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Finds user name by his login.
     * @param login user login.
     * @return user name.
     * @throws ServiceException
     */
    default String userNameByLogin(String login) throws ServiceException {
        throw new ServiceException();
    }
}
