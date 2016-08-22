package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.utils.AuthorsAndTagsInfo;
import com.epam.newsmanagement.utils.InfoUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * For the only controller method
 * retrieving authors and tags for dropdown.
 */
@Controller
public class DropdownController {
    private static final Logger logger = Logger.getLogger(DropdownController.class);

    @Autowired
    private InfoUtils infoUtils;


    @RequestMapping(value = "/authors-and-tags/all", method = RequestMethod.GET)
    @ResponseBody
    public AuthorsAndTagsInfo getAuthorsAndTags() throws ServiceException {
        logger.info("Authors and tags GET request");

        return infoUtils.getAuthorsAndTagsInfo();
    }
}
