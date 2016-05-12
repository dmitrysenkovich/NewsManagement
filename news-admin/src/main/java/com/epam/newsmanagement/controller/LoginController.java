package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Login controller. Responsible
 * for admin authorization.
 */
@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
    public ModelAndView login() {
        System.out.println(userRepository);
        return new ModelAndView("login");
    }
}
