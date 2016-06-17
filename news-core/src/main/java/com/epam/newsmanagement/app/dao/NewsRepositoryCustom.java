package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.utils.SearchCriteria;

import java.util.List;

/**
 * Interface with methods
 * implemented manually.
 */
public interface NewsRepositoryCustom {
    /**
     * Retrieves all news specified
     * by SEARCH_CRITERIA_QUERY string
     * defined in service layer.
     * @param SEARCH_CRITERIA_QUERY search criteria query.
     * @return all fit news.
     *
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
     * Counts news pages satisfying
     * search criteria.
     * @param COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY count news pages by search criteria query.
     * @return news count.
     */
    Long countPagesBySearchCriteria(final String COUNT_PAGES_BY_SEARCH_CRITERIA_QUERY);

    /**
     * Returns news row number in search query
     * satisfying search criteria.
     * @param ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY row number by search criteria query.
     * @return news row number.
     */
    Long rowNumberBySearchCriteria(final String ROW_NUMBER_BY_SEARCH_CRITERIA_QUERY);
}
