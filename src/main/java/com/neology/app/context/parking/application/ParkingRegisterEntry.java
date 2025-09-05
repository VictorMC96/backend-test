package com.neology.app.context.parking.application;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.neology.app.context.parking.domain.ParkingRegister;
import com.neology.app.context.parking.domain.ParkingRegisterRepository;
import com.neology.app.context.vehicle.domain.Vehicle;
import com.neology.app.context.vehicle.domain.VehicleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParkingRegisterEntry {

    private final ParkingRegisterRepository parkingRegisterRepository;
    private final VehicleRepository vehicleRepository;

    public void save(String plate) {
        Vehicle vehicle = this.vehicleRepository.findById(plate).orElseThrow(
                () -> new IllegalArgumentException("the plate not exists"));

        var register = this.parkingRegisterRepository.findByPlateAndIsOpenTrue(plate);

        if (register.isPresent()) {
            throw new IllegalArgumentException("The entry already exist");
        }

        this.parkingRegisterRepository.save(new ParkingRegister(
                plate,
                vehicle.getType(),
                Calendar.getInstance(),
                null,
                true

        ));
    }

}
