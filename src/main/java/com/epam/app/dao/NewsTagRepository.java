package com.epam.app.dao;

import com.epam.app.exception.DaoException;
import com.epam.app.model.News;
import com.epam.app.model.NewsTag;
import com.epam.app.model.Tag;

import java.util.List;

/**
 * NewsTag repository interface.
 */
public interface NewsTagRepository extends CdRepository<NewsTag> {
    /**
     * Adds some tags to news.
     * @param news news to add tags to.
     * @param tags tags to be added.
     * @throws DaoException
     */
    default void addAll(News news, List<Tag> tags) throws DaoException {
        throw new DaoException();
    }
}
