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
        try {
            em = HibernateUtil.getEntityManager();

            Object result = em.createNativeQuery("SELECT 1").getSingleResult();
            System.out.println("MySQL connection OK. SELECT 1 = " + result);
            System.out.println("Hibernate schema initialization completed.");
        } catch (Exception e) {
            System.err.println("Hibernate/MySQL initialization FAILED.");
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            HibernateUtil.shutdown();
        }

        System.out.println("=== Hibernate initialized successfully ===");
    }
}
