package com.epam.app.dao;

import java.util.List;

/**
 * SCRUD repository interface. Defines
 * the contract of a repository with all
 * basic operations and search opportunity.
 */
public interface ScrudRepository<T> extends CrudRepository<T> {
    /**
     * Retrieves all news specified
     * by SEARCH_CRITERIA_QUERY string
     * defined in service layer.
     * @param SEARCH_CRITERIA_QUERY search criteria.
     * @return all fit news.
     */
    List<T> search(final String SEARCH_CRITERIA_QUERY);
}
