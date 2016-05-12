package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.dao.UserRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Role;
import com.epam.newsmanagement.app.model.User;
import com.epam.newsmanagement.app.service.impl.UserServiceImpl;
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
 * User service test.
 */
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void added() throws Exception {
        User user = new User();
        user.setUserId(1L);
        Role role = new Role();
        role.setRoleId(1L);
        when(userRepository.add(user)).thenReturn(1L);
        user = userService.add(user, role);

        assertEquals((Long) 1L, user.getUserId());
        assertEquals((Long) 1L, user.getRoleId());
    }


    @Test(expected = ServiceException.class)
    public void notAdded() throws Exception {
        Role role = new Role();
        role.setRoleId(1L);
        doThrow(new DaoException()).when(userRepository).add(any(User.class));
        userService.add(new User(), role);
    }


    @Test
    public void found() throws Exception {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.find(1L)).thenReturn(user);
        user = userService.find(1L);

        assertEquals((Long) 1L, user.getUserId());
    }


    @Test(expected = ServiceException.class)
    public void notFound() throws Exception {
        doThrow(new DaoException()).when(userRepository).find(any(Long.class));
        userService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        doNothing().when(userRepository).update(any(User.class));
        userService.update(new User());
    }


    @Test(expected = ServiceException.class)
    public void notUpdated() throws Exception {
        doThrow(new DaoException()).when(userRepository).update(any(User.class));
        userService.update(new User());
    }


    @Test
    public void deleted() throws Exception {
        doNothing().when(userRepository).delete(any(User.class));
        userService.delete(new User());
    }


    @Test(expected = ServiceException.class)
    public void notDeleted() throws Exception {
        doThrow(new DaoException()).when(userRepository).delete(any(User.class));
        userService.delete(new User());
    }
}
