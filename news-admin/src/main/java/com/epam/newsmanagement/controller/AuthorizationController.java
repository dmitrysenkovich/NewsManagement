package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.service.UserService;
import com.epam.newsmanagement.utils.AuthorizationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adds csrf token.
 */
@Controller
public class AuthorizationController {
    private static final Logger logger = Logger.getLogger(AuthorizationController.class);

    @Autowired
    private AuthorizationUtils authorizationUtils;
    @Autowired
    private UserService userService;


    /**
     * Sets csrf token.
     * @param request http request.
     * @param response http response.
     */
    @RequestMapping(value = "/csrf", method = RequestMethod.GET)
    @ResponseBody
    public void csrf(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Csrf GET request");

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class
                .getName());
        response.setHeader("X-CSRF-HEADER", csrfToken.getHeaderName());
        response.setHeader("X-CSRF-PARAM", csrfToken.getParameterName());
        response.setHeader("X-CSRF-TOKEN", csrfToken.getToken());
    }


    /**
     * Gets authenticated user name.
     * @throws ServiceException
     * @return user name.
     */
    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> userName() throws ServiceException {
        logger.info("User name GET request");

        String userName = "";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            String login = authorizationUtils.getCurrentUserLogin();
            userName = userService.userNameByLogin(login);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);
        HttpStatus httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(userName, httpHeaders, httpStatus);
    }
}
