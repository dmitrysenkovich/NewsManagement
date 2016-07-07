package com.epam.newsmanagement.app.service.impl;

import com.epam.newsmanagement.app.dao.TagRepository;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.TagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    @Transactional(rollbackFor = ServiceException.class)
    public Tag add(Tag tag) throws ServiceException {
        logger.info("Adding new tag..");
        try {
            tag = tagRepository.save(tag);
        } catch (DataAccessException e) {
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
            tag = tagRepository.findOne(tagId);
        } catch (DataAccessException e) {
            logger.error("Failed to find tag");
            throw new ServiceException(e);
        }
        logger.info("Successfully found tag");
        return tag;
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Tag tag) throws ServiceException {
        logger.info("Updating tag..");
        try {
            tagRepository.save(tag);
        } catch (DataAccessException e) {
            logger.error("Failed to update tag");
            throw new ServiceException(e);
        }
        logger.info("Successfully updated tag");
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Tag tag) throws ServiceException {
        logger.info("Deleting tag..");
        try {
            tag = tagRepository.findOne(tag.getTagId());
            tagRepository.delete(tag);
        } catch (DataAccessException e) {
            logger.error("Failed to delete tag");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted tag");
    }


    @Override
    public List<Tag> findAllByNews(News news) throws ServiceException {
        logger.info("Retrieving news tags..");
        List<Tag> tagsByNews;
        try {
            tagsByNews = tagRepository.findAllByNews(news);
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve news tags");
            throw new ServiceException(e);
        }

        logger.info("Successfully retrieved news tags");
        return tagsByNews;
    }


    @Override
    public List<Tag> findAll() throws ServiceException {
        logger.info("Retrieving all tags..");
        List<Tag> allTags;
        try {
            allTags = tagRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve all tags");
            throw new ServiceException(e);
        }

        logger.info("Successfully retrieved all tags");
        return allTags;
    }


    @Override
    public boolean exists(Tag tag) throws ServiceException {
        logger.info("Checking tag existence..");
        boolean exists;
        try {
            exists = tagRepository.exists(tag.getTagName());
        } catch (DataAccessException e) {
            logger.error("Failed to check tag existence");
            throw new ServiceException(e);
        }

        logger.info("Successfully checked tag existence");
        return exists;
    }
}
