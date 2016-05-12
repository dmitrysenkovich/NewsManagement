package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.Author;

/**
 * Author repository interface.
 */
public interface AuthorRepository extends CrudRepository<Author, Long> {
    /**
     * Makes an author expired.
     * @param author author that is
     * to be expired.
     * @throws DaoException
     */
    void makeAuthorExpired(Author author) throws DaoException;
}
