package com.epam.app.service.impl;

import com.epam.app.dao.NewsAuthorRepository;
import com.epam.app.dao.NewsRepository;
import com.epam.app.dao.NewsTagRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.exception.ServiceException;
import com.epam.app.model.Author;
import com.epam.app.model.News;
import com.epam.app.model.NewsAuthor;
import com.epam.app.model.NewsTag;
import com.epam.app.model.Tag;
import com.epam.app.service.NewsService;
import com.epam.app.utils.SearchCriteria;
import com.epam.app.utils.SearchUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * News service implementation.
 */
public class NewsServiceImpl implements NewsService {
    private static final Logger logger = Logger.getLogger(NewsServiceImpl.class.getName());

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private NewsAuthorRepository newsAuthorRepository;
    @Autowired
    private NewsTagRepository newsTagRepository;

    @Autowired
    private SearchUtils searchUtils;


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public News add(News news, Author author, List<Tag> tags) throws ServiceException {
        logger.info("Adding news..");
        news.setCreationDate(new Timestamp(new java.util.Date().getTime()));
        news.setModificationDate(new Date(new java.util.Date().getTime()));
        try {
            Long id = newsRepository.add(news);
            news.setNewsId(id);
        } catch (DaoException e) {
            logger.error("Failed to add news");
            throw new ServiceException(e);
        }

        if (author != null) {
            logger.info("Adding author to news..");
            NewsAuthor newsAuthor = new NewsAuthor();
            newsAuthor.setNewsId(news.getNewsId());
            newsAuthor.setAuthorId(author.getAuthorId());
            try {
                newsAuthorRepository.add(newsAuthor);
            } catch (DaoException e) {
                logger.error("Failed to add author to news");
                logger.error("Failed to add news");
                throw new ServiceException(e);
            }
            logger.info("Successfully added author to news");
        }

        if (tags != null) {
            logger.info("Adding tags to news..");
            for (Tag tag : tags) {
                NewsTag newsTag = new NewsTag();
                newsTag.setNewsId(news.getNewsId());
                newsTag.setTagId(tag.getTagId());
                try {
                    newsTagRepository.add(newsTag);
                } catch (DaoException e) {
                    logger.error("Failed to add tags to news");
                    logger.error("Failed to add news");
                    throw new ServiceException(e);
                }
            }
            logger.info("Successfully added tags to news");
        }

        logger.info("Successfully added news");
        return news;
    }


    @Override
    public News find(Long newsId) throws ServiceException {
        logger.info("Retrieving news..");
        News news;
        try {
            news = newsRepository.find(newsId);
        } catch (DaoException e) {
            logger.error("Failed to find news");
            throw new ServiceException(e);
        }
        logger.info("Successfully found news");
        return news;
    }


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public void update(News news) throws ServiceException {
        logger.info("Updating news..");
        try {
            newsRepository.update(news);
        } catch (DaoException e) {
            logger.error("Failed to update news");
            throw new ServiceException(e);
        }
        logger.info("Successfully updated news");
    }


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public void delete(News news) throws ServiceException {
        logger.info("Deleting news..");
        try {
            newsRepository.delete(news);
        } catch (DaoException e) {
            logger.error("Failed to delete news");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted news");
    }


    @Override
    public List<News> search(SearchCriteria searchCriteria) throws ServiceException {
        logger.info("Searching certain news..");
        final String SEARCH_CRITERIA_QUERY = searchUtils.getSearchQuery(searchCriteria);
        List<News> fitNews;
        try {
            fitNews = newsRepository.search(SEARCH_CRITERIA_QUERY);
        } catch (DaoException e) {
            logger.error("Failed to find news by search criteria");
            throw new ServiceException(e);
        }
        logger.info("Successfully retrieved news by search criteria");
        return fitNews;
    }


    @Override
    public List<News> findAllSorted() throws ServiceException {
        logger.info("Retrieving all news sorted by comments count..");
        List<News> sortedNews;
        try {
            sortedNews = newsRepository.findAllSorted();
        } catch (DaoException e) {
            logger.error("Failed to retrieve all news sorted by comments count");
            throw new ServiceException(e);
        }
        logger.info("Successfully retrieved all news sorted by comments count");
        return sortedNews;
    }


    @Override
    public Long countAll() throws ServiceException {
        logger.info("Counting all news..");
        Long newsCount;
        try {
            newsCount = newsRepository.countAll();
        } catch (DaoException e) {
            logger.error("Failed to count all news");
            throw new ServiceException(e);
        }
        logger.info("Successfully counted all news");
        return newsCount;
    }
}
