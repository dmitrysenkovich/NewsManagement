package com.epam.app.service.impl;

import com.epam.app.dao.RoleRepository;
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
    @Transactional
    public Role add(Role role) {
        logger.info("Adding new role..");
        role = roleRepository.add(role);
        if (role.getRoleId() != null)
            logger.info("Successfully added new role");
        else
            logger.error("Failed to add new role");
        return role;
    }


    @Override
    public Role find(Long roleId) {
        logger.info("Retrieving role..");
        Role role = roleRepository.find(roleId);
        if (role != null)
            logger.info("Successfully found role");
        else
            logger.error("Failed to find role");
        return role;
    }


    @Override
    @Transactional
    public boolean update(Role role) {
        logger.info("Updating role..");
        boolean updated = roleRepository.update(role);
        if (updated)
            logger.info("Successfully updated role");
        else
            logger.error("Failed to update role");
        return updated;
    }


    @Override
    @Transactional
    public boolean delete(Role role) {
        logger.info("Deleting role..");
        boolean deleted = roleRepository.delete(role);
        if (deleted)
            logger.info("Successfully deleted role");
        else
            logger.error("Failed to delete role");
        return deleted;
    }
}
