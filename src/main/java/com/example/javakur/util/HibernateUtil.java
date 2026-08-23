package com.example.javakur.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class HibernateUtil {

    private static final String PERSISTENCE_UNIT_NAME = "FoodDeliveryPU";
    private static final String DB_PROPERTIES_FILE = "db.properties";
    private static EntityManagerFactory entityManagerFactory;

    private HibernateUtil() {
        // Utility class - instances are not needed.
    }

    private static EntityManagerFactory createProductionEntityManagerFactory() {
        Properties dbProperties = loadDatabaseProperties();

        Map<String, Object> persistenceProperties = new HashMap<>();

        persistenceProperties.put(
                "jakarta.persistence.jdbc.url",
                getRequiredProperty(dbProperties, "db.url")
        );

        persistenceProperties.put(
                "jakarta.persistence.jdbc.user",
                getRequiredProperty(dbProperties, "db.user")
        );

        persistenceProperties.put(
                "jakarta.persistence.jdbc.password",
                getRequiredProperty(dbProperties, "db.password")
        );

        return Persistence.createEntityManagerFactory(
                PERSISTENCE_UNIT_NAME,
                persistenceProperties
        );
    }

    private static Properties loadDatabaseProperties() {
        Properties properties = new Properties();

        try (FileInputStream inputStream =
                     new FileInputStream(DB_PROPERTIES_FILE)) {

            properties.load(inputStream);
            return properties;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not load " + DB_PROPERTIES_FILE +
                            ". Create it in the project root next to pom.xml.",
                    e
            );
        }
    }

    private static String getRequiredProperty(
            Properties properties,
            String key
    ) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required database property: " + key
            );
        }

        return value;
    }

    public static EntityManager getEntityManager() {
        return getProductionEntityManagerFactory().createEntityManager();
    }

    public static EntityManagerFactory getTestEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("FoodDeliveryTestPU");
    }

    public static synchronized void shutdown() {
        if (entityManagerFactory != null) {
            if (entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
            }
            entityManagerFactory = null;
        }
    }

    private static synchronized EntityManagerFactory getProductionEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            entityManagerFactory = createProductionEntityManagerFactory();
        }
        return entityManagerFactory;
    }
}
