package com.epam.app.service.impl;

import com.epam.app.dao.NewsAuthorRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.exception.ServiceException;
import com.epam.app.model.Author;
import com.epam.app.model.News;
import com.epam.app.model.NewsAuthor;
import com.epam.app.service.NewsAuthorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * NewsAuthor service implementation.
 */
public class NewsAuthorServiceImpl implements NewsAuthorService {
    private static final Logger logger = Logger.getLogger(NewsAuthorServiceImpl.class.getName());

    @Autowired
    private NewsAuthorRepository newsAuthorRepository;


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void add(News news, Author author) throws ServiceException {
        logger.info("Adding new author to news..");
        NewsAuthor newsAuthor = new NewsAuthor();
        newsAuthor.setNewsId(news.getNewsId());
        newsAuthor.setAuthorId(author.getAuthorId());
        try {
            newsAuthorRepository.add(newsAuthor);
        } catch (DaoException e) {
            logger.error("Failed to add author to news");
            throw new ServiceException(e);
        }
        logger.info("Successfully added new author to news");
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(News news, Author author) throws ServiceException {
        logger.info("Deleting author from news..");
        NewsAuthor newsAuthor = new NewsAuthor();
        newsAuthor.setNewsId(news.getNewsId());
        newsAuthor.setAuthorId(author.getAuthorId());
        try {
            newsAuthorRepository.delete(newsAuthor);
        } catch (DaoException e) {
            logger.error("Failed to delete author from news");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted author from news");
    }
}
