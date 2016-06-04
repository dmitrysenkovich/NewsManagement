package com.epam.newsmanagement.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Error controller. Dispatches
 * user to page with appropriate
 * error message.
 */
@Controller
public class ErrorController {
    private static final Logger logger = Logger.getLogger(ErrorController.class);


    /**
     * Dispatches access various types of errors.
     * @return error page ModelAndView.
     */
    @RequestMapping(value = {"/400", "/403", "/404", "/500" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView error(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error");
        if (request.getRequestURI().endsWith("403")) {
            logger.info("403 page request");
            modelAndView.addObject("errorMessage", "Hey, you are not admin!:\\");
        }
        else if (request.getRequestURI().endsWith("404")) {
            logger.info("404 page request");
            modelAndView.addObject("errorMessage", "Unfortunately we couldn't find the page you wanted:c");
        }
        else if (request.getRequestURI().endsWith("500")) {
            logger.info("500 page request");
            modelAndView.addObject("errorMessage", "Something terrible happened to us..");
        }
        else {
            logger.info("500 page request");
            modelAndView.addObject("errorMessage", "Something terrible happened to us..");
        }
        return modelAndView;
    }
}
