package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;

import java.util.List;

/**
 * Tag repository.
 */
public interface TagRepository extends CrudRepository<Tag, Long> {
    /**
     * Returns news tags.
     * @param news specifies news
     * which tags are to be retrieved.
     * @return news tags.
     * @throws DaoException
     */
    default List<Tag> getAllByNews(News news) throws DaoException {
        throw new DaoException();
    }

    /**
     * Returns all tags.
     * @return all tags.
     * @throws DaoException
     */
    default List<Tag> getAll() throws DaoException {
        throw new DaoException();
    }

    /**
     * Checks if tag exists.
     * @param tag tag to be checked.
     * @return check result.
     * @throws DaoException
     */
    default boolean exists(Tag tag) throws DaoException {
        throw new DaoException();
    }
}
