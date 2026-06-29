package com.hackathon.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic Data Access Object (DAO) interface.
 * Implements Abstraction by exposing database operations independently of underlying database.
 *
 * @param <T>  The domain entity type
 * @param <ID> The entity's primary key identifier type
 */
public interface GenericDAO<T, ID> {
    
    /**
     * Finds an entity by its unique identifier.
     *
     * @param id The entity's unique identifier
     * @return an Optional containing the found entity, or empty
     */
    Optional<T> findById(ID id);

    /**
     * Retrieves all entities of type T from the database.
     *
     * @return a List of entities
     */
    List<T> findAll();

    /**
     * Persists or updates the entity in the database.
     * If the entity has no ID, it will be inserted (created).
     * If the entity has an ID, it will be updated.
     *
     * @param entity The entity to save
     * @return the saved entity, with any auto-generated fields populated
     */
    T save(T entity);

    /**
     * Deletes an entity by its unique identifier.
     *
     * @param id The identifier of the entity to delete
     * @return true if an entity was deleted, false otherwise
     */
    boolean deleteById(ID id);
}
