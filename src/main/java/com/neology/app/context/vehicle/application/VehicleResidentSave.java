package com.neology.app.context.vehicle.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neology.app.context.account.domain.ResidentAccount;
import com.neology.app.context.account.domain.ResidentAccountRepository;
import com.neology.app.context.vehicle.domain.Vehicle;
import com.neology.app.context.vehicle.domain.VehicleRepository;
import com.neology.app.context.vehicle.domain.VehicleType;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VehicleResidentSave {
    private final VehicleRepository vehicleRepository;
    private final ResidentAccountRepository residentAccountRepository;

    @Transactional
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getType() != VehicleType.RESIDENT) {
            throw new RuntimeException("The vehicle is not resident");
        }

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        residentAccountRepository.save(new ResidentAccount(
                0, savedVehicle));

        return savedVehicle;
    }

}
