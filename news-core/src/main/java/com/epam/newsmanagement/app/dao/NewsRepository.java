package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.News;

import java.util.List;

/**
 * News repository interface.
 */
public interface NewsRepository extends CrudRepository<News, Long> {
    /**
     * Retrieves all news specified
     * by SEARCH_CRITERIA_QUERY string
     * defined in service layer.
     * @param SEARCH_CRITERIA_QUERY search criteria.
     * @return all fit news.
     * @throws DaoException
     *
     */
    List<News> search(final String SEARCH_CRITERIA_QUERY) throws DaoException;

    /**
     * Retrieves all news
     * from database sorted by
     * comments count.
     * @throws DaoException
     * @return all news sorted by comments count.
     */
    List<News> findAllSorted() throws DaoException;

    /**
     * Counts all news.
     * @return news count.
     * @throws DaoException
     */
    Long countAll() throws DaoException;
}
