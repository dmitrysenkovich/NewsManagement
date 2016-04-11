package com.epam.app.service.impl;

import com.epam.app.dao.NewsTagRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.exception.ServiceException;
import com.epam.app.model.Tag;
import com.epam.app.model.News;
import com.epam.app.model.NewsTag;
import com.epam.app.service.NewsTagService;
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
    @Transactional(rollbackFor = DaoException.class)
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
    @Transactional(rollbackFor = DaoException.class)
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
