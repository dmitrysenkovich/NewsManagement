package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;

import java.util.List;

/**
 * Comment repository interface.
 */
public interface CommentRepository extends CrudRepository<Comment, Long> {
    /**
     * Deletes all comments from list.
     * @param comments comments to be deleted.
     * @throws DaoException
     */
    default void deleteAll(List<Comment> comments) throws DaoException {
        throw new DaoException();
    }

    /**
     * Counts news comments count.
     * @param news news which comments
     * will be counted.
     * @return news comments count.
     * @throws DaoException
     */
    default Long countAllByNews(News news) throws DaoException {
        throw new DaoException();
    }

    /**
     * Returns all news comments
     * @param news news which comments
     * will be retrieved.
     * @return news comments.
     * @throws DaoException
     */
    default List<Comment> getAllByNews(News news) throws DaoException {
        throw new DaoException();
    }
}
