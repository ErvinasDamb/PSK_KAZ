package com.example.javakur.service;

import com.example.javakur.exception.ValidationException;
import com.example.javakur.model.RestaurantOwner;
import com.example.javakur.model.User;
import com.example.javakur.repository.UserRepository;
import com.example.javakur.util.PasswordUtil;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RestaurantOwner createRestaurantOwner(String login, String plainPassword, String name, String surname) {
        validateCommonUserFields(login, plainPassword, name, surname);
        if (userRepository.findByLogin(login).isPresent()) {
            throw new ValidationException("Login is already in use");
        }
        RestaurantOwner owner = new RestaurantOwner(
                login.trim(), PasswordUtil.hashPassword(plainPassword), name.trim(), surname.trim());
        return (RestaurantOwner) userRepository.save(owner);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void validateCommonUserFields(String login, String password, String name, String surname) {
        if (login == null || login.isBlank()) throw new ValidationException("Login cannot be blank");
        if (password == null || password.length() < 8) throw new ValidationException("Password must contain at least 8 characters");
        if (name == null || name.isBlank()) throw new ValidationException("Name cannot be blank");
        if (surname == null || surname.isBlank()) throw new ValidationException("Surname cannot be blank");
    }
}
