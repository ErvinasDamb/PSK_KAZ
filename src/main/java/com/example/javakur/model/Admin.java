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
    public Admin(String username, String passwordHash, String name, String surname, String email) {
        super(username, passwordHash, name, surname, email, UserRole.ADMIN);
    }

    @Override
    public String toString() {
        return "[ADMIN] " + super.toString();
    }
}
