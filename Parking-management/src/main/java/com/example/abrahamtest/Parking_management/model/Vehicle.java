package com.example.abrahamtest.Parking_management.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {
    @Id
    private String licensePlate;

    public Vehicle() {}

    public Vehicle(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    // Getters y Setters
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public abstract double calculatePayment(long minutes);
}
