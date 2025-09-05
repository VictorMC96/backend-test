package com.neology.app.context.vehicle.application;

import com.neology.app.context.vehicle.domain.Vehicle;
import com.neology.app.context.vehicle.domain.VehicleRepository;
import com.neology.app.context.vehicle.domain.VehicleType;

public class VehicleOfficialSave {
    private final VehicleRepository vehicleRepository;

    public VehicleOfficialSave(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getType() != VehicleType.OFFICIAL) {
            throw new RuntimeException("The vehicle is not official");
        }

        return vehicleRepository.save(vehicle);
    }

}
