package com.epam.newsmanagement.app.dao.impl;

import com.epam.newsmanagement.app.dao.AuthorRepositoryCustom;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Author repository implementation.
 */
public class AuthorRepositoryImpl implements AuthorRepositoryCustom {
    private static final String GET_ALL_BY_NEWS = "SELECT AUTHOR_ID, AUTHOR_NAME, EXPIRED FROM AUTHORS " +
            "WHERE AUTHOR_ID IN (SELECT AUTHOR_ID FROM NEWS_AUTHOR WHERE NEWS_ID = ?)";
    private static final String ADD_NEWS_AUTHOR_RELATION = "INSERT INTO NEWS_AUTHOR(NEWS_ID, AUTHOR_ID) VALUES(?, ?)";
    private static final String DELETE_ALL_BY_NEWS = "DELETE FROM NEWS_TAG WHERE NEWS_ID = ?";

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Author> getAllByNews(News news) {
        Query query = entityManager.createNativeQuery(GET_ALL_BY_NEWS, Author.class);
        query.setParameter(1, news.getNewsId());
        List<Author> authorsByNews = query.getResultList();
        return authorsByNews;
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
