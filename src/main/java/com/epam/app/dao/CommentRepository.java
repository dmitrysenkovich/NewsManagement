package com.epam.app.dao;

import com.epam.app.exception.DaoException;
import com.epam.app.model.Comment;

import java.util.List;

/**
 * Comment repository interface.
 */
public interface CommentRepository extends CrudaRepository<Comment, Long> {
    /**
     * Deletes all comments from list.
     * @param comments comments to be deleted.
     * @throws DaoException
     */
    void deleteAll(List<Comment> comments) throws DaoException;
}
