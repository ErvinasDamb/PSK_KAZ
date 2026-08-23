package com.example.javakur.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface defining CRUD operations for all entities
 * @param <T> Entity type
 * @param <ID> Primary key type
 */
public interface GenericRepository<T, ID> {

    /**
     * Save or update entity
     * @param entity Entity to save
     * @return Saved entity
     */
    T save(T entity);

    /**
     * Find entity by ID
     * @param id Entity ID
     * @return Optional containing entity if found
     */
    Optional<T> findById(ID id);

    /**
     * Find all entities
     * @return List of all entities
     */
    List<T> findAll();

    /**
     * Update existing entity
     * @param entity Entity to update
     * @return Updated entity
     */
    T update(T entity);

    /**
     * Delete entity
     * @param entity Entity to delete
     */
    void delete(T entity);

    /**
     * Delete entity by ID
     * @param id Entity ID
     */
    void deleteById(ID id);

    /**
     * Check if entity exists by ID
     * @param id Entity ID
     * @return true if exists
     */
    boolean existsById(ID id);

    /**
     * Count all entities
     * @return Total count
     */
    long count();
}