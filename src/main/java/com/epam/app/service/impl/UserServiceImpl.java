package com.epam.app.service.impl;

import com.epam.app.dao.UserRepository;
import com.epam.app.model.Role;
import com.epam.app.model.User;
import com.epam.app.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * User service implementation.
 */
public class UserServiceImpl implements UserService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional
    public User add(User user, Role role) {
        logger.info("Adding new user..");
        user.setRoleId(role.getRoleId());
        user = userRepository.add(user);
        if (user.getUserId() != null)
            logger.info("Successfully added new user");
        else
            logger.error("Failed to add new user");
        return user;
    }


    @Override
    public User find(Long userId) {
        logger.info("Retrieving user..");
        User user = userRepository.find(userId);
        if (user != null)
            logger.info("Successfully found user");
        else
            logger.error("Failed to find user");
        return user;
    }


    @Override
    @Transactional
    public boolean update(User user) {
        logger.info("Updating user..");
        boolean updated = userRepository.update(user);
        if (updated)
            logger.info("Successfully updated user");
        else
            logger.error("Failed to update user");
        return updated;
    }


    @Override
    @Transactional
    public boolean delete(User user) {
        logger.info("Deleting user..");
        boolean deleted = userRepository.delete(user);
        if (deleted)
            logger.info("Successfully deleted user");
        else
            logger.error("Failed to delete user");
        return deleted;
    }
}
