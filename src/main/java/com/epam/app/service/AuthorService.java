package com.epam.app.service;

import com.epam.app.exception.ServiceException;
import com.epam.app.model.Author;

/**
 * Author service interface.
 */
public interface AuthorService extends CrudService<Author> {
    /**
     * Makes an author expired
     * setting expired date current.
     * @param author author that is
     * to be expired.
     * @throws ServiceException
     */
    void makeAuthorExpired(Author author) throws ServiceException;
}
