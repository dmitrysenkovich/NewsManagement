package com.epam.app.service.impl;

import com.epam.app.dao.TagRepository;
import com.epam.app.model.News;
import com.epam.app.model.Tag;
import com.epam.app.service.TagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Tag service implementation.
 */
public class TagServiceImpl implements TagService {
    private static Logger logger = Logger.getLogger(TagServiceImpl.class.getName());

    @Autowired
    private TagRepository tagRepository;


    @Transactional
    public Tag add(Tag tag) {
        logger.info("Adding new tag..");
        tag = tagRepository.add(tag);
        if (tag.getTagId() != null)
            logger.info("Successfully added new tag");
        else
            logger.error("Failed to add new tag");
        return tag;
    }


    public Tag find(Long tagId) {
        logger.info("Retrieving tag..");
        Tag tag = tagRepository.find(tagId);
        if (tag != null)
            logger.info("Successfully found tag");
        else
            logger.error("Failed to find tag");
        return tag;
    }


    @Transactional
    public boolean update(Tag tag) {
        logger.info("Updating tag..");
        boolean updated = tagRepository.update(tag);
        if (updated)
            logger.info("Successfully updated tag");
        else
            logger.error("Failed to update tag");
        return updated;
    }


    @Transactional
    public boolean delete(Tag tag) {
        logger.info("Deleting tag..");
        boolean deleted = tagRepository.delete(tag);
        if (deleted)
            logger.info("Successfully deleted tag");
        else
            logger.error("Failed to delete tag");
        return deleted;
    }


    @Transactional
    public List<Tag> addAll(List<Tag> tags) {
        logger.info("Adding tags..");
        tags = tagRepository.addAll(tags);
        boolean allAdded = true;
        for (Tag tag : tags)
            if (tag.getTagId() == null)
                allAdded = false;
        if (allAdded)
            logger.info("Successfully added tags");
        else
            logger.error("Failed to add tags");
        return tags;
    }
}
