package com.estacionamiento.app.estacionamiento.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.estacionamiento.app.estacionamiento.entities.Vehiculo;

public interface VehiculoRepository extends CrudRepository<Vehiculo, Long>{

	// Query con ordenamiento
    @Query("SELECT p FROM Vehiculo p WHERE p.placa= :placa")
    Vehiculo findByPlaca(@Param("placa") String placa);
}
