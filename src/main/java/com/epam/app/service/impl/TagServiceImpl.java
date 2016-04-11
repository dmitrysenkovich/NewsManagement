package com.epam.app.service.impl;

import com.epam.app.dao.TagRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.exception.ServiceException;
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


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public Tag add(Tag tag) throws ServiceException {
        logger.info("Adding new tag..");
        try {
            tag = tagRepository.add(tag);
        } catch (DaoException e) {
            logger.error("Failed to add new tag");
            throw new ServiceException(e);
        }
        logger.info("Successfully added new tag");
        return tag;
    }


    @Override
    public Tag find(Long tagId) throws ServiceException {
        logger.info("Retrieving tag..");
        Tag tag;
        try {
            tag = tagRepository.find(tagId);
        } catch (DaoException e) {
            logger.error("Failed to find tag");
            throw new ServiceException(e);
        }
        logger.info("Successfully found tag");
        return tag;
    }


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public void update(Tag tag) throws ServiceException {
        logger.info("Updating tag..");
        try {
            tagRepository.update(tag);
        } catch (DaoException e) {
            logger.error("Failed to update tag");
            throw new ServiceException(e);
        }
        logger.info("Successfully updated tag");
    }


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public void delete(Tag tag) throws ServiceException {
        logger.info("Deleting tag..");
        try {
            tagRepository.delete(tag);
        } catch (DaoException e) {
            logger.error("Failed to delete tag");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted tag");
    }


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public List<Tag> addAll(List<Tag> tags) throws ServiceException {
        logger.info("Adding tags..");
        try {
            tags = tagRepository.addAll(tags);
        } catch (DaoException e) {
            logger.error("Failed to add tags");
            throw new ServiceException(e);
        }
        logger.info("Successfully added tags");
        return tags;
    }
}
