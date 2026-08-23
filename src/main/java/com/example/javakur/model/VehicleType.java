package com.example.javakur.model;

public enum VehicleType {
    CAR("Automobilis"),
    MOTORCYCLE("Motociklas"),
    BICYCLE("Dviratis"),
    SCOOTER("Paspirtukas"),
    OTHER("Kita");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
