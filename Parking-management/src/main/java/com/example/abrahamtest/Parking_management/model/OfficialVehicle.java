package com.example.abrahamtest.Parking_management.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OFFICIAL")
public class OfficialVehicle extends Vehicle {

    public OfficialVehicle() {}

    public OfficialVehicle(String licensePlate) {
        super(licensePlate);
    }

    @Override
    public double calculatePayment(long minutes) {
        return 0;
    }
}
