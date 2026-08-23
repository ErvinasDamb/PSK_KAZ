package com.example.javakur;

import com.example.javakur.model.MenuItem;
import com.example.javakur.model.Restaurant;
import com.example.javakur.model.RestaurantOwner;
import com.example.javakur.model.RestaurantType;
import com.example.javakur.repository.MenuItemRepository;
import com.example.javakur.repository.RestaurantRepository;
import com.example.javakur.repository.UserRepository;
import com.example.javakur.service.MenuService;
import com.example.javakur.service.RestaurantService;
import com.example.javakur.service.UserService;
import com.example.javakur.util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Manual MySQL CRUD smoke test for the first M02 vertical slice.
 * Test data is removed in reverse relationship order after the check.
 */
public class MysqlCrudSmoke {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        RestaurantRepository restaurantRepository = new RestaurantRepository();
        MenuItemRepository menuItemRepository = new MenuItemRepository();

        UserService userService = new UserService(userRepository);
        RestaurantService restaurantService = new RestaurantService(restaurantRepository);
        MenuService menuService = new MenuService(menuItemRepository);

        RestaurantOwner owner = null;
        Restaurant restaurant = null;
        MenuItem menuItem = null;
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());

        try {
            owner = userService.createRestaurantOwner(
                    "owner_" + uniqueSuffix,
                    "password123",
                    "Test",
                    "Owner",
                    "owner_" + uniqueSuffix + "@example.com"
            );
            System.out.println("CREATE owner OK, id=" + owner.getId());

            restaurant = restaurantService.createRestaurant(
                    "CRUD Restaurant " + uniqueSuffix,
                    "M02 MySQL smoke test",
                    "Testų g. 1, Vilnius",
                    RestaurantType.OTHER,
                    LocalTime.of(9, 0),
                    LocalTime.of(22, 0),
                    owner
            );
            System.out.println("CREATE restaurant OK, id=" + restaurant.getId());

            menuItem = menuService.createMenuItem(
                    "CRUD Dish",
                    "M02 test dish",
                    new BigDecimal("9.99"),
                    restaurant
            );
            System.out.println("CREATE menu item OK, id=" + menuItem.getId());

            MenuItem storedItem = menuService.findById(menuItem.getId())
                    .orElseThrow(() -> new IllegalStateException("Created menu item was not found"));
            System.out.println("READ menu item OK, price=" + storedItem.getPrice());

            menuItem = menuService.updatePrice(storedItem, new BigDecimal("11.49"));
            MenuItem updatedItem = menuService.findById(menuItem.getId())
                    .orElseThrow(() -> new IllegalStateException("Updated menu item was not found"));

            if (updatedItem.getPrice().compareTo(new BigDecimal("11.49")) != 0) {
                throw new IllegalStateException("Menu item price update was not persisted");
            }

            System.out.println("UPDATE menu item OK, price=" + updatedItem.getPrice());
            System.out.println("=== MYSQL CRUD SMOKE TEST PASSED ===");
        } finally {
            deleteTestData(menuItem, restaurant, owner,
                    menuItemRepository, restaurantRepository, userRepository);
            HibernateUtil.shutdown();
        }
    }

    private static void deleteTestData(
            MenuItem menuItem,
            Restaurant restaurant,
            RestaurantOwner owner,
            MenuItemRepository menuItemRepository,
            RestaurantRepository restaurantRepository,
            UserRepository userRepository
    ) {
        if (menuItem != null && menuItem.getId() != null) {
            menuItemRepository.deleteById(menuItem.getId());
            System.out.println("DELETE menu item OK");
        }
        if (restaurant != null && restaurant.getId() != null) {
            restaurantRepository.deleteById(restaurant.getId());
            System.out.println("DELETE restaurant OK");
        }
        if (owner != null && owner.getId() != null) {
            userRepository.deleteById(owner.getId());
            System.out.println("DELETE owner OK");
        }
    }
}
