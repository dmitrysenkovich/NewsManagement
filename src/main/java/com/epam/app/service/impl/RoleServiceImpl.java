package com.epam.app.service.impl;

import com.epam.app.dao.RoleRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.exception.ServiceException;
import com.epam.app.model.Role;
import com.epam.app.service.RoleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Role service implementation.
 */
public class RoleServiceImpl implements RoleService {
    private static Logger logger = Logger.getLogger(RoleServiceImpl.class.getName());

    @Autowired
    private RoleRepository roleRepository;


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public Role add(Role role) throws ServiceException {
        logger.info("Adding new role..");
        try {
            role = roleRepository.add(role);
        } catch (DaoException e) {
            logger.error("Failed to add new role");
            throw new ServiceException(e);
        }
        logger.info("Successfully added new role");
        return role;
    }


    @Override
    public Role find(Long roleId) throws ServiceException {
        logger.info("Retrieving role..");
        Role role;
        try {
            role = roleRepository.find(roleId);
        } catch (DaoException e) {
            logger.error("Failed to find role");
            throw new ServiceException(e);
        }
        logger.info("Successfully found role");
        return role;
    }


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public void update(Role role) throws ServiceException {
        logger.info("Updating role..");
        try {
            roleRepository.update(role);
        } catch (DaoException e) {
            logger.error("Failed to update role");
            throw new ServiceException(e);
        }
        logger.info("Successfully updated role");
    }


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public void delete(Role role) throws ServiceException {
        logger.info("Deleting role..");
        try {
            roleRepository.delete(role);
        } catch (DaoException e) {
            logger.error("Failed to delete role");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted role");
    }
}
