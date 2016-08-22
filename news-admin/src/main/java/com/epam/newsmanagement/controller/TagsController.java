package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.TagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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


    /**
     * Retrieves all tags.
     * @return all tags.
     * @throws ServiceException
     */
    @RequestMapping(value = "/tags/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Tag> getAll() throws ServiceException {
        logger.info("All tags GET request");

        return tagService.findAll();
    }


    /**
     * Adds new tag.
     * @param tag to the database.
     * @return new tag id if there's
     * no author with such name.
     * @throws ServiceException
     */
    @RequestMapping(value = "/tags/add", method = RequestMethod.PUT)
    @ResponseBody
    public Long add(@RequestBody Tag tag) throws ServiceException {
        logger.info("Add tag PUT request");

        boolean exists = tagService.exists(tag);
        if (!exists) {
            Tag savedTag = tagService.add(tag);
            return savedTag.getTagId();
        }
        return null;
    }


    /**
     * Updates tag name.
     * @param tag tag to be updated.
     * @return true if there's no
     * tag with new tag's name.
     * @throws ServiceException
     */
    @RequestMapping(value = "/tags/update", method = RequestMethod.PUT)
    @ResponseBody
    public boolean update(@RequestBody Tag tag) throws ServiceException {
        logger.info("Update tag PUT request");

        boolean exists = tagService.exists(tag);
        if (!exists)
            tagService.update(tag);
        return exists;
    }


    /**
     * Deletes tag.
     * @param tagId tagId of the tag
     * to be deleted.
     * @throws ServiceException
     */
    @RequestMapping(value = "/tags/delete/{tagId}", method = RequestMethod.POST)
    @ResponseBody
    public void delete(@PathVariable Long tagId) throws ServiceException {
        logger.info("Delete tag POST request");

        Tag tag = new Tag();
        tag.setTagId(tagId);
        tagService.delete(tag);
    }
}
