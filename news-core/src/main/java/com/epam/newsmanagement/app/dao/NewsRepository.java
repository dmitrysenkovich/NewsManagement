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
    default List<News> search(final String SEARCH_CRITERIA_QUERY) throws DaoException {
        throw new DaoException();
    }

    /**
     * Retrieves all news
     * from database sorted by
     * comments count.
     * @throws DaoException
     * @return all news sorted by comments count.
     */
    default List<News> findAllSorted() throws DaoException {
        throw new DaoException();
    }

    /**
     * Counts all news.
     * @return news count.
     * @throws DaoException
     */
    default Long countAll() throws DaoException {
        throw new DaoException();
    }

    /**
     * Counts news pages satisfying
     * search criteria.
     * @param COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY count news pages by search criteria query.
     * @return news count.
     * @throws DaoException
     */
    default Long countPagesBySearchCriteria(final String COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY) throws DaoException {
        throw new DaoException();
    }

    /**
     * Deletes all news with id from list.
     * @param newsIds ids of news to be deleted.
     * @throws DaoException
     */
    default void deleteAll(List<Long> newsIds) throws DaoException {
        throw new DaoException();
    }
}
