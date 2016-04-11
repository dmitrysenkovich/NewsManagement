package com.epam.app.service;

import com.epam.app.dao.RoleRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.exception.ServiceException;
import com.epam.app.model.Role;
import com.epam.app.service.impl.RoleServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        when(roleRepository.add(role)).thenReturn(role);
        role = roleService.add(role);

        assertEquals((Long) 1L, role.getRoleId());
    }


    @Test(expected = ServiceException.class)
    public void notAdded() throws Exception {
        doThrow(new DaoException()).when(roleRepository).add(any(Role.class));
        roleService.add(new Role());
    }


    @Test
    public void found() throws Exception {
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.find(1L)).thenReturn(role);
        role = roleService.find(1L);

        assertEquals((Long) 1L, role.getRoleId());
    }


    @Test(expected = ServiceException.class)
    public void notFound() throws Exception {
        doThrow(new DaoException()).when(roleRepository).find(any(Long.class));
        roleService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        doNothing().when(roleRepository).update(any(Role.class));
        roleService.update(new Role());
    }


    @Test(expected = ServiceException.class)
    public void notUpdated() throws Exception {
        doThrow(new DaoException()).when(roleRepository).update(any(Role.class));
        roleService.update(new Role());
    }


    @Test
    public void deleted() throws Exception {
        doNothing().when(roleRepository).delete(any(Role.class));
        roleService.delete(new Role());
    }


    @Test(expected = ServiceException.class)
    public void notDeleted() throws Exception {
        doThrow(new DaoException()).when(roleRepository).delete(any(Role.class));
        roleService.delete(new Role());
    }
}
