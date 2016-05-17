package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.User;

/**
 * User repository interface.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Finds user name by his login.
     * @param login user login.
     * @return user name.
     * @throws DaoException
     */
    default String userNameByLogin(String login) throws DaoException {
        throw new DaoException();
    }
}
