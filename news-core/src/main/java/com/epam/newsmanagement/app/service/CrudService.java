package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.exception.ServiceException;

/**
 * CRUD service interface.
 */
public interface CrudService<T> {
    /**
     * Adds new entity.
     * @param entity new entity.
     * @return this new entity with id set
     * if added successfully.
     * @throws ServiceException
     */
    default T add(T entity) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Finds an entity by its id.
     * @param id id of the sought-for entity.
     * @return the entity if the needed
     * entity was found, otherwise null.
     * @throws ServiceException
     */
    default T find(Long id) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Updates an entity.
     * @param t an entity to be updated.
     * @throws ServiceException
     */
    default void update(T t) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Deletes an entity.
     * @param t the entity to be deleted.
     * @throws ServiceException
     */
    default void delete(T t) throws ServiceException {
        throw new ServiceException();
    }
}
