package com.example.javakur.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("CLIENT")
@Getter
@Setter
@NoArgsConstructor
public class Client extends User {

    @Column(name = "delivery_address", length = 500)
    private String deliveryAddress;

    @Column(name = "bonus_points")
    private int bonusPoints;

    public Client(String login, String passwordHash, String name, String surname, String deliveryAddress) {
        super(login, passwordHash, name, surname, UserRole.CLIENT);
        this.deliveryAddress = deliveryAddress;
    }

    public void addBonusPoints(int points) {
        if (points < 0) throw new IllegalArgumentException("Bonus points to add cannot be negative");
        bonusPoints += points;
    }

    @Override
    public String toString() {
        return "[CLIENT] " + super.toString() + " | Bonus: " + bonusPoints;
    }
}
