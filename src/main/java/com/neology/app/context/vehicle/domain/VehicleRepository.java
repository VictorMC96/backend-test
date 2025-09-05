package com.neology.app.context.vehicle.domain;

import java.util.Optional;

public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findById(String plate);

}
