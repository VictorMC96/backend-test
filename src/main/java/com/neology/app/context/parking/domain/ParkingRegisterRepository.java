package com.neology.app.context.parking.domain;

import java.util.Optional;

import com.neology.app.context.vehicle.domain.VehicleType;

public interface ParkingRegisterRepository {
    ParkingRegister save(ParkingRegister parking);

    Optional<ParkingRegister> findByPlateAndIsOpenTrue(String plate);

    void deleteByRegisteredType(VehicleType registeredType);
}
