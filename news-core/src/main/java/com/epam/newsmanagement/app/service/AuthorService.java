package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;

import java.util.List;

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
    default void makeAuthorExpired(Author author) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Returns news authors.
     * @param news specifies news
     * which authors are to be retrieved.
     * @return news authors.
     * @throws DaoException
     */
    default List<Author> getAllByNews(News news) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Returns all not expired authors.
     * @return all not expired authors.
     * @throws ServiceException
     */
    default List<Author> getNotExpired() throws ServiceException {
        throw new ServiceException();
    }
}
