package com.neology.estacionamiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neology.estacionamiento.model.VehiculoNoResidente;

public interface VehiculoNoResidenteRepository extends JpaRepository<VehiculoNoResidente, String> {

}
