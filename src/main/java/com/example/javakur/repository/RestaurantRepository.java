package com.example.javakur.repository;

import com.example.javakur.model.Restaurant;
import com.example.javakur.model.RestaurantOwner;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RestaurantRepository extends BaseRepository<Restaurant, Long> {
    public RestaurantRepository() {
        super(Restaurant.class);
    }

    public List<Restaurant> findByOwner(RestaurantOwner owner) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT r FROM Restaurant r WHERE r.owner = :owner ORDER BY r.name", Restaurant.class)
                    .setParameter("owner", owner)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
