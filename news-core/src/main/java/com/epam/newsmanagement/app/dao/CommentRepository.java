package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;

import java.util.List;

/**
 * Root interface for comment repositories.
 */
public interface CommentRepository extends CrudRepository<Comment, Long> {
    /**
     * Deletes all comments from list.
     * @param comments comments to be deleted.
     */
    void deleteAll(List<Comment> comments);

    /**
     * Counts news comments count.
     * @param news news which comments
     * will be counted.
     * @return news comments count.
     */
    Long countAllByNews(News news);

    /**
     * Returns all news comments
     * @param news news which comments
     * will be retrieved.
     * @return news comments.
     */
    List<Comment> findAllByNews(News news);
}
