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
}
