package com.epam.app.service.impl;

import com.epam.app.dao.RoleRepository;
import com.epam.app.model.Role;
import com.epam.app.service.RoleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Role service implementation.
 */
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = Logger.getLogger(RoleServiceImpl.class.getName());

    @Autowired
    private RoleRepository roleRepository;


    public Role add(Role role) {
        logger.info("Adding new role..");
        role = roleRepository.add(role);
        if (role.getRoleId() != 0)
            logger.info("Successfully added new role");
        else
            logger.error("Failed to add new role");
        return role;
    }


    public Role find(int roleId) {
        logger.info("Reprieving role..");
        Role role = roleRepository.find(roleId);
        if (role != null)
            logger.info("Successfully found role");
        else
            logger.error("Failed to find role");
        return role;
    }


    public boolean update(Role role) {
        logger.info("Updating role..");
        boolean updated = roleRepository.update(role);
        if (updated)
            logger.info("Successfully updated role");
        else
            logger.error("Failed to update role");
        return updated;
    }


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
