package com.epam.newsmanagement.app.dao.jpa.custom;

import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;

import java.util.List;

/**
 * Interface with methods
 * implemented manually.
 */
public interface CommentRepositoryJpaCustom {
    /**
     * Returns all news comments
     * @param news news which comments
     * will be retrieved.
     * @return news comments.
     */
    List<Comment> findAllByNews(News news);
}
