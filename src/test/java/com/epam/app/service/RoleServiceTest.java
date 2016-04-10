package com.epam.app.service;

import com.epam.app.dao.impl.RoleRepositoryImpl;
import com.epam.app.model.Role;
import com.epam.app.service.impl.RoleServiceImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Role service test
 */
public class RoleServiceTest {
    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepositoryImpl roleRepository;

    @Mock
    private Logger logger;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(RoleServiceImpl.class, "logger", logger);
    }

    @Test
    public void notAdded() {
        Role role = new Role();
        when(roleRepository.add(role)).thenReturn(role);
        role = roleService.add(role);

        assertNull(role.getRoleId());
        verify(logger).error(eq("Failed to add new role"));
    }

    @Test
    public void added() {
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.add(role)).thenReturn(role);
        role = roleService.add(role);

        assertEquals((Long) 1L, role.getRoleId());
        verify(logger).info(eq("Successfully added new role"));
    }

    @Test
    public void notFound() {
        when(roleRepository.find(1L)).thenReturn(null);
        Role role = roleService.find(1L);

        assertNull(role);
        verify(logger).error(eq("Failed to find role"));
    }

    @Test
    public void found() {
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.find(1L)).thenReturn(role);
        role = roleService.find(1L);

        assertEquals((Long) 1L, role.getRoleId());
        verify(logger).info(eq("Successfully found role"));
    }

    @Test
    public void notUpdated() {
        when(roleRepository.update(null)).thenReturn(false);
        boolean updated = roleService.update(null);

        assertFalse(updated);
        verify(logger).error(eq("Failed to update role"));
    }

    @Test
    public void updated() {
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.update(role)).thenReturn(true);
        boolean updated = roleService.update(role);

        assertTrue(updated);
        verify(logger).info(eq("Successfully updated role"));
    }

    @Test
    public void notDeleted() {
        when(roleRepository.delete(null)).thenReturn(false);
        boolean deleted = roleService.delete(null);

        assertFalse(deleted);
        verify(logger).error(eq("Failed to delete role"));
    }

    @Test
    public void deleted() {
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.delete(role)).thenReturn(true);
        boolean deleted = roleService.delete(role);

        assertTrue(deleted);
        verify(logger).info(eq("Successfully deleted role"));
    }
}
