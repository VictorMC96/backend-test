package com.estacionamiento.parking.repository;

import com.estacionamiento.parking.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {}