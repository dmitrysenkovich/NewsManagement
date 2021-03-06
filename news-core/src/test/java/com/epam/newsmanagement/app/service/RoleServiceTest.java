package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.dao.RoleRepository;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Role;
import com.epam.newsmanagement.app.service.impl.RoleServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.RecoverableDataAccessException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Role service test
 */
public class RoleServiceTest {
    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void added() throws Exception {
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.save(role)).thenReturn(role);
        role = roleService.add(role);

        assertEquals((Long) 1L, role.getRoleId());
    }


    @Test(expected = ServiceException.class)
    public void didNotAdd() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(roleRepository).save(any(Role.class));
        roleService.add(new Role());
    }


    @Test
    public void found() throws Exception {
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.findOne(1L)).thenReturn(role);
        role = roleService.find(1L);

        assertEquals((Long) 1L, role.getRoleId());
    }


    @Test(expected = ServiceException.class)
    public void didNotFind() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(roleRepository).findOne(any(Long.class));
        roleService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        when(roleRepository.save(any(Role.class))).thenReturn(new Role());
        roleService.update(new Role());
    }


    @Test(expected = ServiceException.class)
    public void didNotUpdate() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(roleRepository).save(any(Role.class));
        roleService.update(new Role());
    }


    @Test
    public void deleted() throws Exception {
        doNothing().when(roleRepository).delete(any(Role.class));
        roleService.delete(new Role());
    }


    @Test(expected = ServiceException.class)
    public void didNotDelete() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(roleRepository).delete(any(Role.class));
        roleService.delete(new Role());
    }
}
