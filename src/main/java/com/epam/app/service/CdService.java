package com.epam.app.service;

import com.epam.app.exception.ServiceException;

/**
 * Cd service interface.
 */
public interface CdService<T, E> {
    /**
     * Adds new entity.
     * @param e another entity to be updated.
     * @param t new entity.
     */
    void add(E e, T t) throws ServiceException;

    /**
     * Deletes an entity.
     * @param e another entity to be updated.
     * @param t the entity to be deleted.
     */
    void delete(E e, T t) throws ServiceException;
}
