package com.epam.app.service.impl;

import com.epam.app.dao.AuthorRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.exception.ServiceException;
import com.epam.app.model.Author;
import com.epam.app.service.AuthorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author service.
 */
public class AuthorServiceImpl implements AuthorService {
    private static final Logger logger = Logger.getLogger(AuthorServiceImpl.class.getName());

    @Autowired
    private AuthorRepository authorRepository;


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public Author add(Author author) throws ServiceException {
        logger.info("Adding new author..");
        try {
            author = authorRepository.add(author);
        } catch (DaoException e) {
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
            author = authorRepository.find(authorId);
        } catch (DaoException e) {
            logger.error("Failed to find author");
            throw new ServiceException(e);
        }

        logger.info("Successfully found author");
        return author;
    }


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public void update(Author author) throws ServiceException {
        logger.info("Updating author..");
        try {
            authorRepository.update(author);
        } catch (DaoException e) {
            logger.error("Failed to update author");
            throw new ServiceException(e);
        }
        logger.info("Successfully updated author");
    }


    @Override
    @Transactional(rollbackFor = DaoException.class)
    public void delete(Author author) throws ServiceException {
        logger.info("Deleting author..");
        try {
            authorRepository.delete(author);
        } catch (DaoException e) {
            logger.error("Failed to delete author");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted author");
    }
}
