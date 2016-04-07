package com.epam.app.service;

/**
 * Cd service interface.
 */
public interface CdService<T, E> {
    /**
     * Adds new entity.
     * @param e another entity to be updated.
     * @param t new entity.
     * @return true if added successfully.
     */
    boolean add(E e, T t);

    /**
     * Deletes an entity.
     * @param e another entity to be updated.
     * @param t the entity to be deleted.
     * @return true if deleted successfully.
     */
    boolean delete(E e, T t);
}
