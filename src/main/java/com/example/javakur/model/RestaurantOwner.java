package com.example.javakur.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("RESTAURANT_OWNER")
@Getter
@Setter
@NoArgsConstructor
public class RestaurantOwner extends User {
    public RestaurantOwner(String login, String passwordHash, String name, String surname) {
        super(login, passwordHash, name, surname, UserRole.RESTAURANT_OWNER);
    }

    @Override
    public String toString() {
        return "[OWNER] " + super.toString();
    }
}
