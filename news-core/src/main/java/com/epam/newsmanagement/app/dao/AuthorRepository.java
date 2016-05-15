package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;

import java.util.List;

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
    default void makeAuthorExpired(Author author) throws DaoException {
        throw new DaoException();
    }

    /**
     * Returns news authors.
     * @param news specifies news
     * which authors are to be retrieved.
     * @return news authors.
     * @throws DaoException
     */
    default List<Author> getAllByNews(News news) throws DaoException {
        throw new DaoException();
    }

    /**
     * Returns all not expired authors.
     * @return all not expired authors.
     * @throws DaoException
     */
    default List<Author> getNotExpired() throws DaoException {
        throw new DaoException();
    }
}
