package com.example.javakur.repository;

import com.example.javakur.model.MenuItem;
import com.example.javakur.model.Restaurant;
import jakarta.persistence.EntityManager;

import java.util.List;

public class MenuItemRepository extends BaseRepository<MenuItem, Long> {
    public MenuItemRepository() {
        super(MenuItem.class);
    }

    public List<MenuItem> findByRestaurant(Restaurant restaurant) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM MenuItem m WHERE m.restaurant = :restaurant ORDER BY m.name", MenuItem.class)
                    .setParameter("restaurant", restaurant)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<MenuItem> findAvailableByRestaurant(Restaurant restaurant) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM MenuItem m WHERE m.restaurant = :restaurant AND m.available = true ORDER BY m.name",
                    MenuItem.class)
                    .setParameter("restaurant", restaurant)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
