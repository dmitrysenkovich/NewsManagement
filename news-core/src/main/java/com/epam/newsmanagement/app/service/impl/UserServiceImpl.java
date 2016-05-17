package com.epam.newsmanagement.app.service.impl;

import com.epam.newsmanagement.app.dao.UserRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Role;
import com.epam.newsmanagement.app.model.User;
import com.epam.newsmanagement.app.service.UserService;
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
    @Transactional(rollbackFor = ServiceException.class)
    public User add(User user, Role role) throws ServiceException {
        logger.info("Adding new user..");
        user.setRoleId(role.getRoleId());
        try {
            Long id = userRepository.add(user);
            user.setUserId(id);
        } catch (DaoException e) {
            logger.error("Failed to add new user");
            throw new ServiceException(e);
        }
        logger.info("Successfully added new user");
        return user;
    }


    @Override
    public User find(Long userId) throws ServiceException {
        logger.info("Retrieving user..");
        User user;
        try {
            user = userRepository.find(userId);
        } catch (DaoException e) {
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
            userRepository.update(user);
        } catch (DaoException e) {
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
        } catch (DaoException e) {
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
        } catch (DaoException e) {
            logger.error("Failed to find user name by login");
            throw new ServiceException(e);
        }
        logger.info("Successfully found user name by login");
        return userName;
    }
}
