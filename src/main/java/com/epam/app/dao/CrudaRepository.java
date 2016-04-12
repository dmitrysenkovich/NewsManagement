package com.epam.app.dao;

import com.epam.app.exception.DaoException;

import java.util.List;

/**
 * CRUDA repository interface. Just
 * like in CD repository interface
 * case I don't know a better way
 * to name this repository. Adds
 * opportunity to CRUD repository
 * to add list of entities in
 * database.
 */
public interface CrudaRepository<T, E> extends CrudRepository<T, E> {
    /**
     * Adds all entities from list.
     * @param entities entities to be added.
     * @return entities id list
     * if successfully.
     */
    List<Long> addAll(List<T> entities) throws DaoException;
}
