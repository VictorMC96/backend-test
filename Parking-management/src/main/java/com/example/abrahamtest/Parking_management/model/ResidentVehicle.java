package com.example.abrahamtest.Parking_management.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("RESIDENT")
public class ResidentVehicle extends Vehicle{
    private long totalParkedMinutes;
    public ResidentVehicle() {}

    public ResidentVehicle(String licensePlate) {
        super(licensePlate);
        this.totalParkedMinutes = 0;
    }

    public void addParkedMinutes(long minutes) {
        this.totalParkedMinutes += minutes;
    }

    public void resetParkedMinutes() {
        this.totalParkedMinutes = 0;
    }
    @Override
    public double calculatePayment(long minutes) {
        return minutes*0.05;

    }
}
