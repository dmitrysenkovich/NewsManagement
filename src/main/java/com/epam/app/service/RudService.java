package com.epam.app.service;

import com.epam.app.exception.ServiceException;

/**
 * FUD service interface.
 */
public interface RudService<T> {

    /**
     * Finds an entity by its id.
     * @param id id of the sought-for entity.
     * @return the entity if the needed
     * entity was found, otherwise null.
     */
    T find(Long id) throws ServiceException;

    /**
     * Updates an entity.
     * @param t an entity to be updated.
     */
    void update(T t) throws ServiceException;

    /**
     * Deletes an entity.
     * @param t the entity to be deleted.
     */
    void delete(T t) throws ServiceException;
}
