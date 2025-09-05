package com.neology.app.context.parking.application;

import org.springframework.stereotype.Service;

import com.neology.app.context.parking.domain.ParkingRegisterRepository;
import com.neology.app.context.vehicle.domain.VehicleType;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParkingStartingMonth {

    private final ParkingRegisterRepository parkingRegisterRepository;

    public void run() {
        this.parkingRegisterRepository.deleteByRegisteredType(VehicleType.OFFICIAL);
    }

}
