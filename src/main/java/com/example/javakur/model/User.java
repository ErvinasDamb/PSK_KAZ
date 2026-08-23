package com.example.javakur.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false, length = 100)
    private String login;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "surname", nullable = false, length = 100)
    private String surname;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private UserRole role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    protected User(String login, String passwordHash, String name, String surname, UserRole role) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    @PrePersist
    protected void ensureCreatedAt() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return name + " " + surname + " (" + login + ")";
    }
}
