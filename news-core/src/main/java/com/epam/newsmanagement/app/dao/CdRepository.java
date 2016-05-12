package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.exception.DaoException;

/**
 * CD repository interface. Just
 * like CRUD but CD:) I don't
 * know a better way to name it:c
 */
public interface CdRepository<T> {
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
}
