package com.example.javakur;

import com.example.javakur.model.*;
import com.example.javakur.repository.MenuItemRepository;
import com.example.javakur.repository.RestaurantRepository;
import com.example.javakur.repository.UserRepository;
import com.example.javakur.service.MenuService;
import com.example.javakur.service.RestaurantService;
import com.example.javakur.service.UserService;
import com.example.javakur.util.HibernateUtil;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceFoundationTest {
    private static EntityManagerFactory emf;

    @BeforeAll
    static void init() {
        emf = HibernateUtil.getTestEntityManagerFactory();
    }

    @AfterAll
    static void close() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    @Test
    void passwordHashingWorks() {
        var hash = com.example.javakur.util.PasswordUtil.hashPassword("password123");
        assertNotEquals("password123", hash);
        assertTrue(com.example.javakur.util.PasswordUtil.verifyPassword("password123", hash));
        assertFalse(com.example.javakur.util.PasswordUtil.verifyPassword("wrong", hash));
    }

    @Test
    void restaurantOwnerRestaurantAndMenuItemCanBePersisted() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            RestaurantOwner owner = new RestaurantOwner(
                    "owner1",
                    com.example.javakur.util.PasswordUtil.hashPassword("password123"),
                    "Vardenis",
                    "Pavardenis",
                    "owner1@example.com"
            );
            em.persist(owner);

            Restaurant restaurant = new Restaurant(
                    "Test Restaurant",
                    "Integration test restaurant",
                    "Testų g. 1",
                    RestaurantType.OTHER,
                    LocalTime.of(9, 0),
                    LocalTime.of(22, 0),
                    owner
            );
            em.persist(restaurant);

            MenuItem menuItem = new MenuItem(
                    "Test Dish",
                    "Integration test menu item",
                    new BigDecimal("9.99"),
                    restaurant
            );
            em.persist(menuItem);
            em.getTransaction().commit();

            assertNotNull(owner.getId());
            assertNotNull(restaurant.getId());
            assertNotNull(menuItem.getId());
            assertEquals(owner.getId(), restaurant.getOwner().getId());
            assertEquals(restaurant.getId(), menuItem.getRestaurant().getId());
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
        }
    }
}
