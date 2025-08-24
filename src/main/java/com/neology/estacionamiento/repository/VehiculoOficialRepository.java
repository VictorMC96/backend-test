package com.neology.estacionamiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neology.estacionamiento.model.VehiculoOficial;

public interface VehiculoOficialRepository extends JpaRepository<VehiculoOficial, String> {

}