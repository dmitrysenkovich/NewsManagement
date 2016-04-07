package com.epam.app.dao;

/**
 * CD repository. Just like CRUD
 * but CD:) I don't know a better
 * way to name it:c
 */
public interface CdRepository<T> {
    /**
     * Adds new entity.
     * @param t new entity.
     * @return true if added successfully.
     */
    boolean add(T t);

    /**
     * Deletes an entity.
     * @param t the entity to be deleted.
     * @return true if deleted successfully.
     */
    boolean delete(T t);
}
