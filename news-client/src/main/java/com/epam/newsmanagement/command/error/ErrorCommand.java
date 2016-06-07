package com.epam.newsmanagement.command.error;

import com.epam.newsmanagement.command.Command;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Error command. Generates error page.
 */
public class ErrorCommand implements Command {
    private static final Logger logger = Logger.getLogger(ErrorCommand.class);

    @Resource(name = "messageSource")
    private MessageSource messageSource;


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
        String errorMessage;
        String language = (String) request.getSession().getAttribute("language");
        if (language == null)
            language = "en_US";
        Locale locale = new Locale(language.substring(0, 2));
        switch (uri) {
            case "/news-client/400":
                logger.info("400 page request");
                errorMessage = messageSource.getMessage("error.bad_request", null, locale);
                break;
            case "/news-client/403":
                logger.info("404 page request");
                errorMessage = messageSource.getMessage("error.denied", null, locale);
                break;
            case "/news-client/404":
                logger.info("404 page request");
                errorMessage = messageSource.getMessage("error.not_found", null, locale);
                break;
            case "/news-client/500":
                logger.info("500 page request");
                errorMessage = messageSource.getMessage("error.internal_error", null, locale);
                break;
            default:
                logger.info("Unknown error");
                errorMessage = messageSource.getMessage("error.internal_error", null, locale);
        }
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/error.tiles").forward(request, response);
    }
}
