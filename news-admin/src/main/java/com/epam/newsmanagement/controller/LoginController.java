package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.utils.AuthorizationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Login controller. Responsible
 * for admin authorization.
 */
@Controller
public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    private AuthorizationUtils authorizationUtils;


    /**
     * Dispatches login requests.
     * @param error error while authorizing.
     * @param logout message while login out.
     * @return login ModelAndView.
     */
    @RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              HttpServletRequest request) throws ServiceException {
        logger.info("Login GET request");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            String login = authorizationUtils.getCurrentUserLogin();
            if (login != null) {
                if (request.isUserInRole("ADMIN"))
                    return new ModelAndView("redirect:news-list-administration");
                else
                    return new ModelAndView("redirect:/404");
            }
        }

        ModelAndView model = new ModelAndView();
        if (error != null) {
            logger.info("Admin login failed");
            model.addObject("error", "0");
        }
        if (logout != null) {
            logger.info("Admin logged out");
            model.addObject("logout", "0");
        }
        model.setViewName("login");
        return model;
    }
}
