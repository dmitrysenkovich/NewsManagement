package com.epam.newsmanagement.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Dispatches requests for certain pages.
 */
@Controller
public class PagesController {
    private static final Logger logger = Logger.getLogger(PagesController.class);


    /**
     * Dispatches requests to home page.
     * @return home page name.
     */
    @RequestMapping(value = { "/", "/login**", "/news-list", "/news/**", "/authors", "/tags", "/add", "/edit/**" }, method = RequestMethod.GET)
    public String home() {
        logger.info("Home GET request");

        return "home";
    }
}
