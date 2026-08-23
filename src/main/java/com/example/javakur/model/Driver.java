package com.example.javakur.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("DRIVER")
@Getter
@Setter
@NoArgsConstructor
public class Driver extends User {

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", length = 50)
    private VehicleType vehicleType;

    public Driver(String login, String passwordHash, String name, String surname, VehicleType vehicleType) {
        super(login, passwordHash, name, surname, UserRole.DRIVER);
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return "[DRIVER] " + super.toString() + " | Vehicle: " + vehicleType;
    }
}
