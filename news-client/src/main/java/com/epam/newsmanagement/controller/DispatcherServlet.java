package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.command.Command;
import com.epam.newsmanagement.utils.CommandBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Dispatches all requests.
 */
@WebServlet(urlPatterns = "/news-management/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    @Autowired
    private CommandBuilder commandBuilder;


    /**
     * Initializes servlet and spring context.
     * @param config servlet config.
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Command command = commandBuilder.getCommand(request);
        command.execute(request, response);
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Command command = commandBuilder.getCommand(request);
        command.execute(request, response);
    }
}
