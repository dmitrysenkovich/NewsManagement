package com.epam.app.dao;

import com.epam.app.model.Comment;

import java.util.List;

/**
 * Comment repository interface.
 */
public interface CommentRepository extends CrudaRepository<Comment, Long> {
    /**
     * Deletes all comments from list.
     * @param comments comments to be deleted.
     * @return true if deleted successfully.
     */
    boolean deleteAll(List<Comment> comments);
}
