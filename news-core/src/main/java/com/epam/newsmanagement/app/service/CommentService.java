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
   default Comment add(News news, Comment comment) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Deletes all comments from list.
     * @param comments comments to be deleted.
     * @throws ServiceException
     */
    default void deleteAll(List<Comment> comments) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Counts all news comments.
     * @param news news which comments
     * will be counted.
     * @return news count. -1 if not
     * finished successfully.
     * @throws ServiceException
     */
    default Long countAllByNews(News news) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Returns all news comments
     * @param news news which comments
     * will be retrieved.
     * @return news comments.
     * @throws ServiceException
     */
    default List<Comment> findAllByNews(News news) throws ServiceException {
        throw new ServiceException();
    }
}
