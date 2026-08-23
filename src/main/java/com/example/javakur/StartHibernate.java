package com.example.javakur;

import com.example.javakur.util.HibernateUtil;
import jakarta.persistence.EntityManager;

/**
 * Development bootstrap used to initialize Hibernate against the local MySQL DB.
 * Running this class should create/update the tables defined by the JPA entities.
 */
public class StartHibernate {

    public static void main(String[] args) {
        System.out.println("=== Starting Hibernate with MySQL ===");

        EntityManager em = null;
        boolean successful = false;
        try {
            em = HibernateUtil.getEntityManager();

            Object result = em.createNativeQuery("SELECT 1").getSingleResult();
            System.out.println("MySQL connection OK. SELECT 1 = " + result);
            System.out.println("Hibernate schema initialization completed.");
            successful = true;
        } catch (Exception e) {
            System.err.println("Hibernate/MySQL initialization FAILED.");
            e.printStackTrace(System.err);
            Throwable rootCause = findRootCause(e);
            System.err.println();
            System.err.println("Root cause: " + rootCause.getClass().getName()
                    + ": " + rootCause.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            HibernateUtil.shutdown();
        }

        if (!successful) {
            System.exit(1);
        }

        System.out.println("=== Hibernate initialized successfully ===");
    }

    private static Throwable findRootCause(Throwable throwable) {
        Throwable result = throwable;
        while (result.getCause() != null && result.getCause() != result) {
            result = result.getCause();
        }
        return result;
    }
}
