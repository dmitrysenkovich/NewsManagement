package com.epam.newsmanagement.app.service.impl;

import com.epam.newsmanagement.app.dao.RoleRepository;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Role;
import com.epam.newsmanagement.app.service.RoleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Role service implementation.
 */
public class RoleServiceImpl implements RoleService {
    private static Logger logger = Logger.getLogger(RoleServiceImpl.class.getName());

    @Autowired
    private RoleRepository roleRepository;


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Role add(Role role) throws ServiceException {
        logger.info("Adding new role..");
        Role savedRole;
        try {
            savedRole = roleRepository.save(role);
        } catch (DataAccessException e) {
            logger.error("Failed to add new role");
            throw new ServiceException(e);
        }
        logger.info("Successfully added new role");
        return savedRole;
    }


    @Override
    public Role find(Long roleId) throws ServiceException {
        logger.info("Retrieving role..");
        Role role;
        try {
            role = roleRepository.findOne(roleId);
        } catch (DataAccessException e) {
            logger.error("Failed to find role");
            throw new ServiceException(e);
        }
        logger.info("Successfully found role");
        return role;
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Role role) throws ServiceException {
        logger.info("Updating role..");
        try {
            roleRepository.save(role);
        } catch (DataAccessException e) {
            logger.error("Failed to update role");
            throw new ServiceException(e);
        }
        logger.info("Successfully updated role");
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Role role) throws ServiceException {
        logger.info("Deleting role..");
        try {
            roleRepository.delete(role);
        } catch (DataAccessException e) {
            logger.error("Failed to delete role");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted role");
    }
}
