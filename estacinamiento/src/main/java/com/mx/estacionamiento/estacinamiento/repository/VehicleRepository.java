package com.mx.estacionamiento.estacinamiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mx.estacionamiento.estacinamiento.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {

}
