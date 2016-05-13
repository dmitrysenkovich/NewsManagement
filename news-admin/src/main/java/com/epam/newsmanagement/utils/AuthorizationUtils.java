package com.epam.newsmanagement.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Manages authorization stuff.
 */
public class AuthorizationUtils {
    /**
     * Returns login as username of current user.
     * @return current user login.
     */
    public String getCurrentUserLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String login = userDetails.getUsername();
        return login;
    }
}
