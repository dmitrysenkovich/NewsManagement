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
     * @return this new entity with id set.
     */
    T add(T t) throws DaoException;

    /**
     * Finds an entity by its id.
     * @param id id of the sought-for entity.
     * @return the entity if the needed
     * entity was found, otherwise null.
     */
    T find(E id) throws DaoException;

    /**
     * Updates an entity.
     * @param t an entity to be updated.
     */
    void update(T t) throws DaoException;

    /**
     * Deletes an entity.
     * @param t the entity to be deleted.
     */
    void delete(T t) throws DaoException;
}
