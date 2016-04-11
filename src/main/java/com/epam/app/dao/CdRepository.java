package com.epam.app.dao;

import com.epam.app.exception.DaoException;

/**
 * CD repository interface. Just
 * like CRUD but CD:) I don't
 * know a better way to name it:c
 */
public interface CdRepository<T> {
    /**
     * Adds new entity.
     * @param t new entity.
     */
    void add(T t) throws DaoException;

    /**
     * Deletes an entity.
     * @param t the entity to be deleted.
     */
    void delete(T t) throws DaoException;
}
