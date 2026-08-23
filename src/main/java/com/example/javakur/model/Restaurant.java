package com.example.javakur.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 500)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RestaurantType type;

    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private RestaurantOwner owner;

    public Restaurant(String name, String description, String address, RestaurantType type,
                      LocalTime openingTime, LocalTime closingTime, RestaurantOwner owner) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.type = type;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
