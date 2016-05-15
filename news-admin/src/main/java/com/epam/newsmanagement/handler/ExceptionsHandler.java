package com.epam.newsmanagement.handler;

import com.epam.newsmanagement.app.exception.ServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * All methods processing
 * exception are here.
 */
public class ExceptionsHandler {
    @ExceptionHandler(ServiceException.class)
    public String handleServiceException() {
        return "redirect:/500";
    }
}
