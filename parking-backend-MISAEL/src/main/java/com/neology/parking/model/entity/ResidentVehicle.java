package com.neology.parking.model.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("RESIDENT")
public class ResidentVehicle extends Vehicle {
    private int accumulatedMinutes = 0;

    public int getAccumulatedMinutes() {
        return accumulatedMinutes;
    }

    public void setAccumulatedMinutes(int accumulatedMinutes) {
        this.accumulatedMinutes = accumulatedMinutes;
    }
}
