package com.epam.newsmanagement.app.dao;

/**
 * CRUD repository interface. Defines
 * the contract of a repository with all
 * basic operations.
 * @param <T> the object type in a repository.
 * @param <E> the identifier type.
 */
public interface CrudRepository<T, E> {
    /**
     * Adds new entity or updates
     * existing one.
     * @param t entity.
     * @return entity with
     * new id if was set.
     */
    T save(T t);

    /**
     * Finds an entity by its id.
     * @param id id of the sought-for entity.
     * @return the entity if the needed
     * entity was found, otherwise null.
     */
    T findOne(E id);

    /**
     * Deletes an entity.
     * @param t the entity to be deleted.
     */
    void delete(T t);
}
