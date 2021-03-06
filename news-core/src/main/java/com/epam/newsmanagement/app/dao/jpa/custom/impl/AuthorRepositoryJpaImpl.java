package com.epam.newsmanagement.app.dao.jpa.custom.impl;

import com.epam.newsmanagement.app.dao.jpa.custom.AuthorRepositoryJpaCustom;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * Author repository implementation.
 */
public class AuthorRepositoryJpaImpl implements AuthorRepositoryJpaCustom {
    private static final String EXISTS = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM AUTHORS WHERE AUTHOR_NAME = ?";
    private static final String FIND_ALL_BY_NEWS = "SELECT AUTHOR_ID, AUTHOR_NAME, EXPIRED FROM AUTHORS " +
            "WHERE AUTHOR_ID IN (SELECT AUTHOR_ID FROM NEWS_AUTHOR WHERE NEWS_ID = ?)";
    private static final String ADD_NEWS_AUTHOR_RELATION = "INSERT INTO NEWS_AUTHOR(NEWS_ID, AUTHOR_ID) VALUES(?, ?)";
    private static final String DELETE_ALL_BY_NEWS = "DELETE FROM NEWS_AUTHOR WHERE NEWS_ID = ?";

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public boolean exists(String authorName) {
        Query query = entityManager.createNativeQuery(EXISTS);
        query.setParameter(1, authorName);
        BigDecimal exists = (BigDecimal) query.getSingleResult();
        if (exists.equals(new BigDecimal(1)))
            return true;
        return false;
    }


    @Override
    public List<Author> findAllByNews(News news) {
        Query query = entityManager.createNativeQuery(FIND_ALL_BY_NEWS, Author.class);
        query.setParameter(1, news.getNewsId());
        return query.getResultList();
    }


    @Override
    public void addAll(News news, List<Author> authors) {
        for (Author author : authors) {
            Query query = entityManager.createNativeQuery(ADD_NEWS_AUTHOR_RELATION);
            query.setParameter(1, news.getNewsId());
            query.setParameter(2, author.getAuthorId());
            query.executeUpdate();
        }
    }


    @Override
    public void deleteAllRelationsByNews(News news) {
        Query query = entityManager.createNativeQuery(DELETE_ALL_BY_NEWS);
        query.setParameter(1, news.getNewsId());
        query.executeUpdate();
    }
}
