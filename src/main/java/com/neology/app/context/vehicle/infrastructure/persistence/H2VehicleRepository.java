package com.neology.app.context.vehicle.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neology.app.context.vehicle.domain.Vehicle;
import com.neology.app.context.vehicle.domain.VehicleRepository;

public interface H2VehicleRepository extends JpaRepository<Vehicle, String>, VehicleRepository {
}
