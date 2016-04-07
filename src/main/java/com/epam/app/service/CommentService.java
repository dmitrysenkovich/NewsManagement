package com.epam.app.service;

import com.epam.app.model.Comment;
import com.epam.app.model.News;

import java.util.List;

/**
 * Comment service interface.
 */
public interface CommentService extends RudService<Comment> {
    /**
     * Adds comment to news.
     * @param news news that will be updated.
     * @param comment new comment.
     * @return comment with set id
     * if added successfully.
     */
    Comment add(News news, Comment comment);

    /**
     * Adds all comments from list to news.
     * @param news news that will be updated.
     * @param comments comments to be added.
     * @return comments with set id
     * if successfully.
     */
    List<Comment> addAll(News news, List<Comment> comments);

    /**
     * Deletes all comments from list.
     * @param comments comments to be deleted.
     * @return true if deleted successfully.
     */
    boolean deleteAll(List<Comment> comments);
}
