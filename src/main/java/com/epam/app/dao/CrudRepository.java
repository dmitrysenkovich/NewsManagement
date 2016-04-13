package com.epam.app.dao;

import com.epam.app.exception.DaoException;

/**
 * CRUD repository interface. Defines
 * the contract of a repository with all
 * basic operations.
 */
public interface CrudRepository<T, E> {
    /**
     * Adds new entity.
     * @param t new entity.
     * @return new entity id.
     * @throws DaoException
     */
    default E add(T t) throws DaoException {
        throw new DaoException();
    }

    /**
     * Finds an entity by its id.
     * @param id id of the sought-for entity.
     * @return the entity if the needed
     * entity was found, otherwise null.
     */
    default T find(E id) throws DaoException {
        throw new DaoException();
    }

    /**
     * Updates an entity.
     * @param t an entity to be updated.
     */
    default void update(T t) throws DaoException {
        throw new DaoException();
    }

    /**
     * Deletes an entity.
     * @param t the entity to be deleted.
     */
    default void delete(T t) throws DaoException {
        throw new DaoException();
    }
}
