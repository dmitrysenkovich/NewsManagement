package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.model.News;

import java.util.List;

/**
 * News dependencies repository.
 * Special interface for tags and authors.
 * Includes operations such as add, delete and
 * bulk operations for them.
 */
public interface NewsDependenciesRepository<T, E> {
    /**
     * Adds new entity.
     * @param t new entity.
     * @throws DaoException
     */
    default void add(T t) throws DaoException {
        throw new DaoException();
    }

    /**
     * Deletes an entity.
     * @param t the entity to be deleted.
     * @throws DaoException
     */
    default void delete(T t) throws DaoException {
        throw new DaoException();
    }

    /**
     * Adds some entities to news.
     * @param news news to add entities to.
     * @param entities entities to be added.
     * @throws DaoException
     */
    default void addAll(News news, List<E> entities) throws DaoException {
        throw new DaoException();
    }

    /**
     * Deletes all news dependencies
     * to certain type.
     * @param news news.
     * @throws DaoException
     */
    default void deleteAll(News news) throws DaoException {
        throw new DaoException();
    }
}
