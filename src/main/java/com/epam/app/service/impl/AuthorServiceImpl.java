package com.epam.app.service.impl;

import com.epam.app.dao.AuthorRepository;
import com.epam.app.model.Author;
import com.epam.app.service.AuthorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author service.
 */
public class AuthorServiceImpl implements AuthorService {
    private static final Logger logger = Logger.getLogger(AuthorServiceImpl.class.getName());

    @Autowired
    private AuthorRepository authorRepository;


    public Author add(Author author) {
        logger.info("Adding new author..");
        author = authorRepository.add(author);
        if (author.getAuthorId() != 0)
            logger.info("Successfully added new author");
        else
            logger.error("Failed to add new author");
        return author;
    }


    public Author find(Long authorId) {
        logger.info("Reprieving author..");
        Author author = authorRepository.find(authorId);
        if (author != null)
            logger.info("Successfully found author");
        else
            logger.error("Failed to find author");
        return author;
    }


    public boolean update(Author author) {
        logger.info("Updating author..");
        boolean updated = authorRepository.update(author);
        if (updated)
            logger.info("Successfully updated author");
        else
            logger.error("Failed to update author");
        return updated;
    }


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
