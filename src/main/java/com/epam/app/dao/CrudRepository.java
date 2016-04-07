package com.epam.app.dao;

/**
 * CrudRepository interface. Should
 * be implemented by every repository.
 */
public interface CrudRepository<T> {
    /**
     * Adds new entity.
     * @param t new entity.
     * @return this new entity with id set.
     */
    T add(T t);

    /**
     * Finds an entity by its id.
     * @param id id of the sought-for entity.
     * @return the entity if the needed
     * entity was found, otherwise null.
     */
    T find(int id);

    /**
     * Updates an entity.
     * @param t an entity to be updated.
     * @return true if updated successfully.
     */
    boolean update(T t);

    /**
     * Deletes an entity.
     * @param t the entity to be deleted.
     * @return true if deleted successfully.
     */
    boolean delete(T t);
}
