package com.neology.app.context.parking.application;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.neology.app.context.parking.domain.ParkingRegisterRepository;
import com.neology.app.context.vehicle.domain.VehicleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParkingRegisterExit {

    private final ParkingRegisterRepository parkingRegisterRepository;
    private final VehicleRepository vehicleRepository;

    public void save(String plate) {
        this.vehicleRepository.findById(plate).orElseThrow(
                () -> new IllegalArgumentException("the plate not exists"));

        var register = this.parkingRegisterRepository.findByPlateAndIsOpenTrue(plate)
                .orElseThrow(() -> new IllegalArgumentException("The entry already exist"));
        register.setExitAt(Calendar.getInstance());
        register.setOpen(false);

        this.parkingRegisterRepository.save(register);
    }

}
