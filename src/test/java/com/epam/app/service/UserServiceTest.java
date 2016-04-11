package com.epam.app.service;

import com.epam.app.dao.UserRepository;
import com.epam.app.model.Role;
import com.epam.app.model.User;
import com.epam.app.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User service test.
 */
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Logger logger;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(UserServiceImpl.class, "logger", logger);
    }

    @Test
    public void added() {
        User user = new User();
        user.setUserId(1L);
        Role role = new Role();
        role.setRoleId(1L);
        when(userRepository.add(user)).thenReturn(user);
        user = userService.add(user, role);

        assertEquals((Long) 1L, user.getUserId());
        assertEquals((Long) 1L, user.getRoleId());
        verify(logger).info(eq("Successfully added new user"));
    }

    @Test
    public void notAdded() {
        User user = new User();
        Role role = new Role();
        when(userRepository.add(user)).thenReturn(user);
        user = userService.add(user, role);

        assertNull(user.getUserId());
        verify(logger).error(eq("Failed to add new user"));
    }

    @Test
    public void found() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.find(1L)).thenReturn(user);
        user = userService.find(1L);

        assertEquals((Long) 1L, user.getUserId());
        verify(logger).info(eq("Successfully found user"));
    }

    @Test
    public void notFound() {
        when(userRepository.find(1L)).thenReturn(null);
        User user = userService.find(1L);

        assertNull(user);
        verify(logger).error(eq("Failed to find user"));
    }

    @Test
    public void updated() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.update(user)).thenReturn(true);
        boolean updated = userService.update(user);

        assertTrue(updated);
        verify(logger).info(eq("Successfully updated user"));
    }

    @Test
    public void notUpdated() {
        when(userRepository.update(null)).thenReturn(false);
        boolean updated = userService.update(null);

        assertFalse(updated);
        verify(logger).error(eq("Failed to update user"));
    }

    @Test
    public void deleted() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.delete(user)).thenReturn(true);
        boolean deleted = userService.delete(user);

        assertTrue(deleted);
        verify(logger).info(eq("Successfully deleted user"));
    }

    @Test
    public void notDeleted() {
        when(userRepository.delete(null)).thenReturn(false);
        boolean deleted = userService.delete(null);

        assertFalse(deleted);
        verify(logger).error(eq("Failed to delete user"));
    }
}
