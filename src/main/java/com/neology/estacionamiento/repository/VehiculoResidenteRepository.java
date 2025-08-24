package com.neology.estacionamiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neology.estacionamiento.model.VehiculoResidente;

public interface VehiculoResidenteRepository extends JpaRepository<VehiculoResidente, String> {
}
