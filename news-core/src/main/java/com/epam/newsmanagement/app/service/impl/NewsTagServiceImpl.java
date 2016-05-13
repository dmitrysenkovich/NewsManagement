package com.epam.newsmanagement.app.service.impl;

import com.epam.newsmanagement.app.dao.NewsTagRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.NewsTag;
import com.epam.newsmanagement.app.service.NewsTagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * NewsTag service implementation.
 */
public class NewsTagServiceImpl implements NewsTagService {
    private static final Logger logger = Logger.getLogger(NewsTagServiceImpl.class.getName());

    @Autowired
    private NewsTagRepository newsTagRepository;


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void add(News news, Tag tag) throws ServiceException {
        logger.info("Adding new tag to news..");
        NewsTag newsTag = new NewsTag();
        newsTag.setNewsId(news.getNewsId());
        newsTag.setTagId(tag.getTagId());
        try {
            newsTagRepository.add(newsTag);
        } catch (DaoException e) {
            logger.error("Failed to add tag to news");
            throw new ServiceException(e);
        }
        logger.info("Successfully added new tag to news");
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(News news, Tag tag) throws ServiceException {
        logger.info("Deleting tag from news..");
        NewsTag newsTag = new NewsTag();
        newsTag.setNewsId(news.getNewsId());
        newsTag.setTagId(tag.getTagId());
        try {
            newsTagRepository.delete(newsTag);
        } catch (DaoException e) {
            logger.error("Failed to delete tag from news");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted tag from news");
    }
}