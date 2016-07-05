package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.User;

/**
 * Root interface for user repositories.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Finds user name by his login.
     * @param login user login.
     * @return user name.
     */
    String userNameByLogin(String login);
}
