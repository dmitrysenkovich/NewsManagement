package com.epam.newsmanagement.handler;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Handles failed user authentication.
 */
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final Logger logger = Logger.getLogger(CustomAuthenticationFailureHandler.class);

    /**
     * Actual failed authentication handler.
     * @param request request while authentication.
     * @param response response.
     * @param exception was thrown to disable user authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                    AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Writer writer = response.getWriter();
        if (exception instanceof DisabledException) {
            logger.error("Access disabled");
            response.getWriter().write("disabled");
        }
        else {
            logger.info("Error while authentication");
            writer.write("invalid");
        }
        writer.flush();
        writer.close();
    }
}
