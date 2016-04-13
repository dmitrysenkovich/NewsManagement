package com.epam.app.service;

import com.epam.app.exception.ServiceException;
import com.epam.app.model.Comment;
import com.epam.app.model.News;

import java.util.List;

/**
 * Comment service interface.
 */
public interface CommentService extends CrudService<Comment> {
    /**
     * Adds comment to news.
     * @param news news that will be updated.
     * @param comment new comment.
     * @return comment with set id
     * if added successfully.
     * @throws ServiceException
     */
    Comment add(News news, Comment comment) throws ServiceException;

    /**
     * Adds all comments from list to news.
     * @param news news that will be updated.
     * @param comments comments to be added.
     * @return comments with set id
     * if successfully.
     * @throws ServiceException
     */
    List<Comment> addAll(News news, List<Comment> comments) throws ServiceException;

    /**
     * Deletes all comments from list.
     * @param comments comments to be deleted.
     * @throws ServiceException
     */
    void deleteAll(List<Comment> comments) throws ServiceException;
}
