package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.News;

import java.util.List;

/**
 * Root interface for news repositories.
 */
public interface NewsRepository extends CrudRepository<News, Long> {
    /**
     * Retrieves all news specified
     * by SEARCH_CRITERIA_QUERY string
     * defined in service layer.
     * @param SEARCH_CRITERIA_QUERY search criteria.
     * @return all fit news.
     */
    List<News> search(final String SEARCH_CRITERIA_QUERY);

    /**
     * Retrieves all news
     * from database sorted by
     * comments count.
     * @return all news sorted by comments count.
     */
    List<News> findAllSorted();

    /**
     * Counts all news.
     * @return news count.
     */
    Long countAll();

    /**
     * Counts news pages satisfying
     * search criteria.
     * @param COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY count news pages by search criteria query.
     * @return news count.
     */
    Long countPagesBySearchCriteria(final String COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY);

    /**
     * Deletes all news with id from list.
     * @param newsIds ids of news to be deleted.
     */
    void deleteAll(List<Long> newsIds);

    /**
     * Returns news row number in search query
     * satisfying search criteria.
     * @param ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY row number by search criteria query.
     * @return news row number.
     */
    Long rowNumberBySearchCriteria(final String ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY);

    /**
     * Stub because of JPA.
     */
    void flush();
}
