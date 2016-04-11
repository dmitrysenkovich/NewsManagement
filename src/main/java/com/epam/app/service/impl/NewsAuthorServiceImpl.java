package com.epam.app.service.impl;

import com.epam.app.dao.NewsAuthorRepository;
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
    private static Logger logger = Logger.getLogger(NewsAuthorServiceImpl.class.getName());

    @Autowired
    private NewsAuthorRepository newsAuthorRepository;


    @Transactional
    public boolean add(News news, Author author) {
        logger.info("Adding new author to news..");
        NewsAuthor newsAuthor = new NewsAuthor();
        newsAuthor.setNewsId(news.getNewsId());
        newsAuthor.setAuthorId(author.getAuthorId());
        boolean added = newsAuthorRepository.add(newsAuthor);
        if (added)
            logger.info("Successfully added new author to news");
        else
            logger.error("Failed to add author to news");
        return added;
    }


    @Transactional
    public boolean delete(News news, Author author) {
        logger.info("Deleting author from news..");
        NewsAuthor newsAuthor = new NewsAuthor();
        newsAuthor.setNewsId(news.getNewsId());
        newsAuthor.setAuthorId(author.getAuthorId());
        boolean deleted = newsAuthorRepository.delete(newsAuthor);
        if (deleted)
            logger.info("Successfully deleted author from news");
        else
            logger.error("Failed to delete author from news");
        return deleted;
    }
}
