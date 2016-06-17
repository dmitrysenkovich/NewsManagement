package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;

import java.util.List;

/**
 * Interface with methods
 * implemented manually.
 */
public interface CommentRepositoryCustom {
    /**
     * Returns all news comments
     * @param news news which comments
     * will be retrieved.
     * @return news comments.
     */
    List<Comment> getAllByNews(News news);
}
