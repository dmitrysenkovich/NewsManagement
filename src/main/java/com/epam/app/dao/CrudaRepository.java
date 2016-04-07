package com.epam.app.dao;

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
public interface CrudaRepository<T> extends CrudRepository<T> {
    /**
     * Adds all entities from list.
     * @param entities entities to be added.
     * @return entities with set id
     * if successfully.
     */
    List<T> addAll(List<T> entities);
}
