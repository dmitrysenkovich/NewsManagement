package com.epam.newsmanagement.app.dao.jpa.custom.impl;

import com.epam.newsmanagement.app.dao.jpa.custom.NewsRepositoryJpaCustom;
import com.epam.newsmanagement.app.model.News;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * News repository implementation.
 */
public class NewsRepositoryJpaImpl implements NewsRepositoryJpaCustom {
    private static final String FIND_ALL_SORTED = "(SELECT NEWS_ID, TITLE, SHORT_TEXT, " +
            "FULL_TEXT, CREATION_DATE, MODIFICATION_DATE, COMMENTS_COUNT " +
            "FROM NEWS JOIN (SELECT NEWS_ID, COUNT(*) COMMENTS_COUNT " +
            "                FROM COMMENTS " +
            "                GROUP BY NEWS_ID) NEWS_STAT USING(NEWS_ID))" +
            "UNION " +
            "(SELECT NEWS_ID, TITLE, SHORT_TEXT, " +
            "FULL_TEXT, CREATION_DATE, MODIFICATION_DATE, 0 COMMENTS_COUNT " +
            "FROM NEWS " +
            "WHERE NEWS_ID NOT IN (SELECT NEWS_ID " +
            "                      FROM COMMENTS))" +
            "ORDER BY COMMENTS_COUNT DESC, MODIFICATION_DATE";

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<News> search(final String SEARCH_CRITERIA_QUERY) {
        Query query = entityManager.createNativeQuery(SEARCH_CRITERIA_QUERY, News.class);
        return query.getResultList();
    }


    @Override
    public List<News> findAllSorted() {
        Query query = entityManager.createNativeQuery(FIND_ALL_SORTED, News.class);
        return query.getResultList();
    }


    @Override
    public Long countPagesBySearchCriteria(final String COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY) {
        Query query = entityManager.createNativeQuery(COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY);
        return ((BigDecimal) query.getSingleResult()).longValue();
    }


    @Override
    public Long rowNumberBySearchCriteria(final String ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY) {
        Query query = entityManager.createNativeQuery(ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY);
        return ((BigDecimal) query.getSingleResult()).longValue();
    }
}
