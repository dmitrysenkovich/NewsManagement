package com.epam.newsmanagement.app.service.impl;

import com.epam.newsmanagement.app.dao.UserRepository;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Role;
import com.epam.newsmanagement.app.model.User;
import com.epam.newsmanagement.app.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

/**
 * User service implementation.
 */
public class UserServiceImpl implements UserService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public User add(User user, Role role) throws ServiceException {
        logger.info("Adding new user..");
        user.setRole(role);
        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (DataAccessException e) {
            logger.error("Failed to add new user");
            throw new ServiceException(e);
        }
        logger.info("Successfully added new user");
        return savedUser;
    }


    @Override
    public User find(Long userId) throws ServiceException {
        logger.info("Retrieving user..");
        User user;
        try {
            user = userRepository.findOne(userId);
        } catch (DataAccessException e) {
            logger.error("Failed to find user");
            throw new ServiceException(e);
        }
        logger.info("Successfully found user");
        return user;
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(User user) throws ServiceException {
        logger.info("Updating user..");
        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            logger.error("Failed to update user");
            throw new ServiceException(e);
        }
        logger.info("Successfully updated user");
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(User user) throws ServiceException {
        logger.info("Deleting user..");
        try {
            userRepository.delete(user);
        } catch (DataAccessException e) {
            logger.error("Failed to delete user");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted user");
    }


    @Override
    public String userNameByLogin(String login) throws ServiceException {
        logger.info("Retrieving user name by login..");
        String userName;
        try {
            userName = userRepository.userNameByLogin(login);
        } catch (DataAccessException e) {
            logger.error("Failed to find user name by login");
            throw new ServiceException(e);
        }
        logger.info("Successfully found user name by login");
        return userName;
    }
}
