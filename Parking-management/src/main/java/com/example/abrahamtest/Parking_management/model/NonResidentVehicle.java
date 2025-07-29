package com.example.abrahamtest.Parking_management.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("NON_RESIDENT")

public class NonResidentVehicle extends Vehicle{

    public NonResidentVehicle () {}

    public NonResidentVehicle(String licensePlate) {
        super(licensePlate);
    }
    @Override
    public double calculatePayment(long minutes) {
        return minutes * 0.5;
    }
}
