package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;

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
     * Deletes all comments from list.
     * @param comments comments to be deleted.
     * @throws ServiceException
     */
    void deleteAll(List<Comment> comments) throws ServiceException;
}
