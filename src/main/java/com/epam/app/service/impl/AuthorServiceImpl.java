package com.epam.app.service.impl;

import com.epam.app.dao.AuthorRepository;
import com.epam.app.model.Author;
import com.epam.app.service.AuthorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author service.
 */
public class AuthorServiceImpl implements AuthorService {
    private static Logger logger = Logger.getLogger(AuthorServiceImpl.class.getName());

    @Autowired
    private AuthorRepository authorRepository;


    @Transactional
    public Author add(Author author) {
        logger.info("Adding new author..");
        author = authorRepository.add(author);
        if (author.getAuthorId() != null)
            logger.info("Successfully added new author");
        else
            logger.error("Failed to add new author");
        return author;
    }


    public Author find(Long authorId) {
        logger.info("Retrieving author..");
        Author author = authorRepository.find(authorId);
        if (author != null)
            logger.info("Successfully found author");
        else
            logger.error("Failed to find author");
        return author;
    }


    @Transactional
    public boolean update(Author author) {
        logger.info("Updating author..");
        boolean updated = authorRepository.update(author);
        if (updated)
            logger.info("Successfully updated author");
        else
            logger.error("Failed to update author");
        return updated;
    }


    @Transactional
    public boolean delete(Author author) {
        logger.info("Deleting author..");
        boolean deleted = authorRepository.delete(author);
        if (deleted)
            logger.info("Successfully deleted author");
        else
            logger.error("Failed to delete author");
        return deleted;
    }
}
