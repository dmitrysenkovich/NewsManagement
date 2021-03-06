package com.epam.newsmanagement.controller;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Error controller. Dispatches
 * user to page with appropriate
 * error message.
 */
@Controller
public class ErrorController {
    private static final Logger logger = Logger.getLogger(ErrorController.class);

    @Resource(name = "messageSource")
    private MessageSource messageSource;


    /**
     * Dispatches access various types of errors.
     * @param request http request.
     * @param locale current locale.
     * @return error message.
     */
    @RequestMapping(value = {"/400", "/403", "/404", "/500" }, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public ResponseEntity<String> error(HttpServletRequest request, Locale locale) {
        String errorMessage;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if (request.getRequestURI().endsWith("400")) {
            logger.info("400 page request");
            errorMessage = messageSource.getMessage("error.bad_request", null, locale);
        }
        else if (request.getRequestURI().endsWith("403")) {
            logger.info("403 page request");
            errorMessage = messageSource.getMessage("error.denied", null, locale);
            httpStatus = HttpStatus.FORBIDDEN;
        }
        else if (request.getRequestURI().endsWith("404")) {
            logger.info("404 page request");
            errorMessage = messageSource.getMessage("error.not_found", null, locale);
            httpStatus = HttpStatus.NOT_FOUND;
        }
        else if (request.getRequestURI().endsWith("500")) {
            logger.info("500 page request");
            errorMessage = messageSource.getMessage("error.internal_error", null, locale);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        else {
            logger.info("Unknown error");
            errorMessage = messageSource.getMessage("error.unknown_error", null, locale);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(errorMessage, httpHeaders, httpStatus);
    }
}
