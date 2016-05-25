package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.TagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Tags controller. Responsible
 * for managing tags.
 */
@Controller
public class TagsController {
    private static final Logger logger = Logger.getLogger(TagsController.class);

    @Autowired
    private TagService tagService;


    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public ModelAndView tags() throws ServiceException {
        logger.info("Tags GET request");

        ModelAndView modelAndView = new ModelAndView("tags");

        List<Tag> tags = tagService.getAll();
        modelAndView.addObject("tags", tags);

        return modelAndView;
    }


    @RequestMapping(value = "/tags/add", method = RequestMethod.POST)
    @ResponseBody
    public Long add(@RequestBody Tag tag) throws ServiceException {
        logger.info("Add tag POST request");

        boolean exists = tagService.exists(tag);
        if (!exists) {
            tag = tagService.add(tag);
            return tag.getTagId();
        }
        return null;
    }


    @RequestMapping(value = "/tags/update", method = RequestMethod.POST)
    @ResponseBody
    public boolean update(@RequestBody Tag tag) throws ServiceException {
        logger.info("Update tag POST request");

        boolean exists = tagService.exists(tag);
        if (!exists)
            tagService.update(tag);
        return exists;
    }


    @RequestMapping(value = "/tags/delete", method = RequestMethod.POST)
    @ResponseBody
    public void delete(@RequestBody Tag tag) throws ServiceException {
        logger.info("Delete tag POST request");

        tagService.delete(tag);
    }
}
