package com.neology.estacionamiento.repository;

import com.neology.estacionamiento.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {
}
