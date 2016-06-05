package com.epam.newsmanagement.app.service.impl;

import com.epam.newsmanagement.app.dao.NewsAuthorRepository;
import com.epam.newsmanagement.app.dao.NewsRepository;
import com.epam.newsmanagement.app.dao.NewsTagRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.NewsAuthor;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.app.utils.SearchUtils;
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
    @Transactional(rollbackFor = ServiceException.class)
    public News add(News news, List<Author> authors, List<Tag> tags) throws ServiceException {
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

        if (authors != null && !authors.isEmpty()) {
            logger.info("Adding authors to news..");

            try {
                newsAuthorRepository.addAll(news, authors);
            } catch (DaoException e) {
                logger.error("Failed to add authors to news");
                logger.error("Failed to add news");
                throw new ServiceException(e);
            }
            logger.info("Successfully added authors to news");
        }
        else {
            logger.error("News must have at least one author");
            logger.error("Failed to add news");
            throw new ServiceException();
        }

        if (tags != null && !tags.isEmpty()) {
            logger.info("Adding tags to news..");

            try {
                newsTagRepository.addAll(news, tags);
            } catch (DaoException e) {
                logger.error("Failed to add tags to news");
                logger.error("Failed to add news");
                throw new ServiceException(e);
            }
            logger.info("Successfully added tags to news");
        }

        logger.info("Successfully added news");
        return news;
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void updateNewsAuthorsAndTags(News news, List<Author> authors, List<Tag> tags) throws ServiceException {
        logger.info("Updating news authors and tags..");

        try {
            logger.info("Deleting all news authors..");
            newsAuthorRepository.deleteAll(news);
        } catch (DaoException e) {
            logger.error("Failed to delete all news authors");
            logger.error("Failed to update news authors and tags");
            throw new ServiceException(e);
        }

        try {
            logger.info("Deleting all news tags..");
            newsTagRepository.deleteAll(news);
        } catch (DaoException e) {
            logger.error("Failed to delete all news tags");
            logger.error("Failed to update news authors and tags");
            throw new ServiceException(e);
        }

        try {
            logger.info("Adding all news to authors relations..");
            newsAuthorRepository.addAll(news, authors);
        } catch (DaoException e) {
            logger.error("Failed to add all news authors");
            logger.error("Failed to update news authors and tags");
            throw new ServiceException(e);
        }

        try {
            logger.info("Adding all news to tags relations..");
            newsTagRepository.addAll(news, tags);
        } catch (DaoException e) {
            logger.error("Failed to add all news tags");
            logger.error("Failed to update news authors and tags");
            throw new ServiceException(e);
        }

        logger.info("Successfully updated news authors and tags");
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
    @Transactional(rollbackFor = ServiceException.class)
    public void update(News news) throws ServiceException {
        news.setModificationDate(new Date(new java.util.Date().getTime()));
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
    @Transactional(rollbackFor = ServiceException.class)
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


    @Override
    public Long countPagesBySearchCriteria(SearchCriteria searchCriteria) throws ServiceException {
        logger.info("Counting news pages by search criteria..");
        final String COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY = searchUtils.getCountQuery(searchCriteria);
        Long fitNewsPagesCount;
        try {
            fitNewsPagesCount = newsRepository.countPagesBySearchCriteria(COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY);
        } catch (DaoException e) {
            logger.error("Failed to count news pages by search criteria");
            throw new ServiceException(e);
        }
        logger.info("Successfully counted news pages by search criteria");
        return fitNewsPagesCount;
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void deleteAll(List<Long> newsIds) throws ServiceException {
        logger.info("Deleting list of news..");
        try {
            newsRepository.deleteAll(newsIds);
        } catch (DaoException e) {
            logger.error("Failed to delete list of news");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted list of news");
    }


    @Override
    public Long rowNumberBySearchCriteria(SearchCriteria searchCriteria, News news) throws ServiceException {
        logger.info("Retrieving news row number by search criteria..");
        final String ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY = searchUtils.getRowNumberQuery(searchCriteria, news.getNewsId());
        Long newsRowNumber;
        try {
            newsRowNumber = newsRepository.rowNumberBySearchCriteria(ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY);
        } catch (DaoException e) {
            logger.error("Failed to retrieve news row number by search criteria");
            throw new ServiceException(e);
        }
        logger.info("Successfully retrieved news row number by search criteria");
        return newsRowNumber;
    }
}
