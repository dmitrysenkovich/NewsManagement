package com.epam.newsmanagement.app.service.impl;

import com.epam.newsmanagement.app.dao.AuthorRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.AuthorService;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Author service.
 */
public class AuthorServiceImpl implements AuthorService {
    private static final Logger logger = Logger.getLogger(AuthorServiceImpl.class.getName());

    @Autowired
    private AuthorRepository authorRepository;


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Author add(Author author) throws ServiceException {
        logger.info("Adding new author..");
        try {
            author = authorRepository.save(author);
        } catch (HibernateException e) {
            logger.error("Failed to add new author");
            throw new ServiceException(e);
        }
        logger.info("Successfully added new author");
        return author;
    }


    @Override
    public Author find(Long authorId) throws ServiceException {
        logger.info("Retrieving author..");
        Author author;
        try {
            author = authorRepository.findOne(authorId);
        } catch (HibernateException e) {
            logger.error("Failed to find author");
            throw new ServiceException(e);
        }

        logger.info("Successfully found author");
        return author;
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Author author) throws ServiceException {
        logger.info("Updating author..");
        try {
            authorRepository.save(author);
        } catch (HibernateException e) {
            logger.error("Failed to update author");
            throw new ServiceException(e);
        }
        logger.info("Successfully updated author");
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Author author) throws ServiceException {
        logger.info("Deleting author..");
        try {
            authorRepository.delete(author);
        } catch (HibernateException e) {
            logger.error("Failed to delete author");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted author");
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void makeAuthorExpired(Author author) throws ServiceException {
        logger.info("Making author expired..");
        try {
            author = authorRepository.findOne(author.getAuthorId());
            author.setExpired(new Timestamp(new java.util.Date().getTime()));
            authorRepository.save(author);
        } catch (HibernateException e) {
            logger.error("Failed to make author expired");
            throw new ServiceException(e);
        }
        logger.info("Successfully made author expired");
    }


    @Override
    public List<Author> getAllByNews(News news) throws ServiceException {
        logger.info("Retrieving news authors..");
        List<Author> authorsByNews;
        try {
            authorsByNews = authorRepository.getAllByNews(news);
        } catch (HibernateException e) {
            logger.error("Failed to retrieve news authors");
            throw new ServiceException(e);
        }

        logger.info("Successfully retrieved news authors");
        return authorsByNews;
    }


    @Override
    public List<Author> getNotExpired() throws ServiceException {
        logger.info("Retrieving not expired authors..");
        List<Author> notExpiredAuthors;
        try {
            notExpiredAuthors = authorRepository.getNotExpired();
        } catch (HibernateException e) {
            logger.error("Failed to retrieve not expired authors");
            throw new ServiceException(e);
        }

        logger.info("Successfully retrieved not expired authors");
        return notExpiredAuthors;
    }


    @Override
    public List<Author> getAll() throws ServiceException {
        logger.info("Retrieving all authors..");
        List<Author> allAuthors;
        try {
            allAuthors = authorRepository.findAll();
        } catch (HibernateException e) {
            logger.error("Failed to retrieve all authors");
            throw new ServiceException(e);
        }

        logger.info("Successfully retrieved all authors");
        return allAuthors;
    }


    @Override
    public boolean exists(Author author) throws ServiceException {
        logger.info("Checking author existence..");
        boolean exists;
        try {
            exists = authorRepository.exists(author.getAuthorName());
        } catch (HibernateException e) {
            logger.error("Failed to check author existence");
            throw new ServiceException(e);
        }

        logger.info("Successfully checked author existence");
        return exists;
    }
}
