package com.neology.parking.service;

import com.neology.parking.model.entity.*;
import com.neology.parking.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> getVehicle(String plate) {
        return vehicleRepository.findById(plate);
    }

    public void resetMonth() {
        for (Vehicle v : vehicleRepository.findAll()) {
            if (v instanceof ResidentVehicle) {
                ((ResidentVehicle) v).setAccumulatedMinutes(0);
                vehicleRepository.save(v);
            }
        }
    }
}
