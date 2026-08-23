package com.example.javakur.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("ADMIN")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {
    public Admin(String login, String passwordHash, String name, String surname) {
        super(login, passwordHash, name, surname, UserRole.ADMIN);
    }

    @Override
    public String toString() {
        return "[ADMIN] " + super.toString();
    }
}
