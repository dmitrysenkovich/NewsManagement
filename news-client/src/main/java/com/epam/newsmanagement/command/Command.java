package com.epam.newsmanagement.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Command interface. Specifies
 * the only method implementing
 * classes must have.
 */
public interface Command {
    /**
     * The only method mentioned above.
     * Does stuff needed to serve client
     * request e.g. update, create some
     * data or whatever it wants to do.
     * @param request request.
     * @param response response.
     */
    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
