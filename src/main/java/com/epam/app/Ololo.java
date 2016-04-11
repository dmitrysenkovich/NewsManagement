package com.epam.app;

import com.epam.app.dao.RoleRepository;
import com.epam.app.model.Role;
import com.epam.app.service.RoleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by dmitry on 4/10/16.
 */
public class Ololo {
    public static void main(String... args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("spring-configuration.xml");

        RoleService roleService = (RoleService) context.getBean("roleService");

        Role role = new Role();
        role.setRoleName("qwe");
        roleService.add(role);
    }
}
