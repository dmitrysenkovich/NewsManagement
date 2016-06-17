package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * User repository interface.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds user name by his login.
     * @param login user login.
     * @return user name.
     */
    @Query("select U.userName from User U where U.login = :login")
    String userNameByLogin(@Param("login") String login);
}
