package com.epam.newsmanagement.handler;

import com.epam.newsmanagement.app.exception.ServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Dispatches internal server error.
 */
public class ExceptionsHandler {
    /**
     * A simple handler for exceptions.
     * @return view for errors.
     */
    @ExceptionHandler(ServiceException.class)
    public String handleServiceException() {
        return "redirect:/500";
    }
}
