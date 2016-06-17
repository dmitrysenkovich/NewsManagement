package com.epam.newsmanagement.app.dao.impl;

import com.epam.newsmanagement.app.dao.CommentRepositoryCustom;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Comment repository implementation.
 */
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private static final String GET_ALL_BY_NEWS = "SELECT COMMENT_ID, NEWS_ID, COMMENT_TEXT, CREATION_DATE " +
            "FROM COMMENTS WHERE NEWS_ID = ? ORDER BY CREATION_DATE";

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Comment> getAllByNews(News news) {
        Query query = entityManager.createNativeQuery(GET_ALL_BY_NEWS, Comment.class);
        query.setParameter(1, news.getNewsId());
        List<Comment> commentsByNews = query.getResultList();
        return commentsByNews;
    }
}
