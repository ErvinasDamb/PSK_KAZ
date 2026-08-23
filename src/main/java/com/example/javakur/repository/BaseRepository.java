package com.example.javakur.repository;

import com.example.javakur.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Base repository implementation with common CRUD operations
 * @param <T> Entity type
 * @param <ID> Primary key type
 */
public abstract class BaseRepository<T, ID> implements GenericRepository<T, ID> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final Class<T> entityClass;

    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Get entity manager
     */
    protected EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    @Override
    public T save(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            logger.debug("Saved entity: {}", entity);
            return entity;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error saving entity", e);
            throw new RuntimeException("Failed to save entity", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        EntityManager em = getEntityManager();
        try {
            T entity = em.find(entityClass, id);
            logger.debug("Found entity by ID {}: {}", id, entity);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            logger.error("Error finding entity by ID: {}", id, e);
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            List<T> results = query.getResultList();
            logger.debug("Found {} entities", results.size());
            return results;
        } catch (Exception e) {
            logger.error("Error finding all entities", e);
            throw new RuntimeException("Failed to find all entities", e);
        } finally {
            em.close();
        }
    }

    @Override
    public T update(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T updated = em.merge(entity);
            em.getTransaction().commit();
            logger.debug("Updated entity: {}", updated);
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error updating entity", e);
            throw new RuntimeException("Failed to update entity", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T managedEntity = em.merge(entity);
            em.remove(managedEntity);
            em.getTransaction().commit();
            logger.debug("Deleted entity: {}", entity);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error deleting entity", e);
            throw new RuntimeException("Failed to delete entity", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(ID id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
                logger.debug("Deleted entity by ID: {}", id);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error deleting entity by ID: {}", id, e);
            throw new RuntimeException("Failed to delete entity by ID", e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    public long count() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(jpql, Long.class).getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting entities", e);
            return 0;
        } finally {
            em.close();
        }
    }

    /**
     * Execute query with transaction
     */
    protected <R> R executeInTransaction(EntityManager em, TransactionCallback<R> callback) {
        try {
            em.getTransaction().begin();
            R result = callback.execute(em);
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    /**
     * Functional interface for transaction callbacks
     */
    @FunctionalInterface
    protected interface TransactionCallback<R> {
        R execute(EntityManager em);
    }
}