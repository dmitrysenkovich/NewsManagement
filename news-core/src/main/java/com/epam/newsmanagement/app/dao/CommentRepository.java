package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.Comment;

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
    void deleteAll(List<Comment> comments) throws DaoException;
}
