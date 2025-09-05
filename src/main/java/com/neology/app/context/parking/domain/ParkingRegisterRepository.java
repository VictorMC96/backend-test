package com.neology.app.context.parking.domain;

import java.util.Optional;

public interface ParkingRegisterRepository {
    ParkingRegister save(ParkingRegister parking);

    Optional<ParkingRegister> findByPlateAndIsOpenTrue(String plate);
}
