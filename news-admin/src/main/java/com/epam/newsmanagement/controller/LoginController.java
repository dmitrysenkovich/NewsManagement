package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.User;
import com.epam.newsmanagement.app.service.UserService;
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


    @RequestMapping(value = "/news-list-administration", method = RequestMethod.GET)
    public ModelAndView newsListAdministrationStub() {
        logger.info("News list administration GET request");
        return new ModelAndView("news-list-administration");
    }


    /**
     * Dispatches access denying.
     * @return error page ModelAndView.
     */
    @RequestMapping(value = { "/403", "/404" }, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView error(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        if (request.getRequestURI().endsWith("403")) {
            logger.info("403 page request");
            modelAndView.addObject("errorMessage", "Hey, you are not admin!:\\");
        }
        else {
            logger.info("404 page request");
            modelAndView.addObject("errorMessage", "Unfortunately we couldn't find the page you wanted:c");
        }
        return modelAndView;
    }
}
