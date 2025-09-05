package com.neology.app.context.vehicle.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neology.app.context.vehicle.domain.Vehicle;
import com.neology.app.context.vehicle.domain.VehicleRepository;
import com.neology.app.context.vehicle.domain.VehicleType;

@Service
public class VehicleOfficialSave {
    private final VehicleRepository vehicleRepository;

    public VehicleOfficialSave(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getType() != VehicleType.OFFICIAL) {
            throw new RuntimeException("The vehicle is not official");
        }

        return vehicleRepository.save(vehicle);
    }

}
