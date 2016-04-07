package com.epam.app.dao;

import com.epam.app.model.News;

import java.util.List;

/**
 * News repository interface.
 */
public interface NewsRepository extends ScrudRepository<News> {
    /**
     * Retrieves all news
     * from database sorted by
     * comments count.
     * @return all news sorted by comments count.
     */
    List<News> findAllSorted();

    /**
     * Counts all news.
     * @return news count. -1 if not
     * finished successfully.
     */
    int countAll();
}
