package com.neology.app.context.vehicle.application;

import org.springframework.stereotype.Service;
import com.neology.app.context.vehicle.domain.Vehicle;
import com.neology.app.context.vehicle.domain.VehicleRepository;
import com.neology.app.context.vehicle.domain.VehicleType;

@Service
public class VehicleResidentSave {
    private final VehicleRepository vehicleRepository;

    public VehicleResidentSave(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getType() != VehicleType.RESIDENT) {
            throw new RuntimeException("The vehicle is not resident");
        }

        return vehicleRepository.save(vehicle);
    }

}
