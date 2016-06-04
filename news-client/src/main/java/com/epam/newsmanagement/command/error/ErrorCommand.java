package com.epam.newsmanagement.command.error;

import com.epam.newsmanagement.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Error command. Generates error page.
 */
public class ErrorCommand implements Command {
    private static final Logger logger = Logger.getLogger(ErrorCommand.class);


    /**
     * Dispatches access various types of errors.
     * @param request request.
     * @param response response.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        switch (uri) {
            case "/news-management/404":
                logger.info("404 page request");
                request.setAttribute("errorMessage", "Unfortunately we couldn't find the page you wanted:c");
                break;
            case "/news-management/400":
                logger.info("400 page request");
            case "/news-management/500":
                logger.info("500 page request");
                request.setAttribute("errorMessage", "Something terrible happened to us..");
                break;
            default:
                logger.info("Unknown error");
                request.setAttribute("errorMessage", "Something terrible happened to us..");
        }
        request.getRequestDispatcher("/error.tiles").forward(request, response);
    }
}
