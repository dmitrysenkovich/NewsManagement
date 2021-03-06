package com.epam.newsmanagement.app.dao.jpa.custom.impl;

import com.epam.newsmanagement.app.dao.jpa.custom.TagRepositoryJpaCustom;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * Tag repository implementation.
 */
public class TagRepositoryJpaImpl implements TagRepositoryJpaCustom {
    private static final String EXISTS = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM TAGS WHERE TAG_NAME = ?";
    private static final String FIND_ALL_BY_NEWS = "SELECT TAG_ID, TAG_NAME FROM TAGS " +
            "WHERE TAG_ID IN (SELECT TAG_ID FROM NEWS_TAG WHERE NEWS_ID = ?)";
    private static final String ADD_NEWS_TAG_RELATION = "INSERT INTO NEWS_TAG(NEWS_ID, TAG_ID) VALUES(?, ?)";
    private static final String DELETE_ALL_BY_NEWS = "DELETE FROM NEWS_TAG WHERE NEWS_ID = ?";

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public boolean exists(String tagName) {
        Query query = entityManager.createNativeQuery(EXISTS);
        query.setParameter(1, tagName);
        BigDecimal exists = ((BigDecimal) query.getSingleResult());
        if (exists.equals(new BigDecimal(1)))
            return true;
        return false;
    }


    @Override
    public List<Tag> findAllByNews(News news) {
        Query query = entityManager.createNativeQuery(FIND_ALL_BY_NEWS, Tag.class);
        query.setParameter(1, news.getNewsId());
        return query.getResultList();
    }


    @Override
    public void addAll(News news, List<Tag> tags) {
        for (Tag tag : tags) {
            Query query = entityManager.createNativeQuery(ADD_NEWS_TAG_RELATION);
            query.setParameter(1, news.getNewsId());
            query.setParameter(2, tag.getTagId());
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
