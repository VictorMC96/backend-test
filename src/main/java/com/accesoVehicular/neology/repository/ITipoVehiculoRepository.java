package com.accesoVehicular.neology.repository;

import com.accesoVehicular.neology.model.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ITipoVehiculoRepository extends JpaRepository<TipoVehiculo, Long> {

    @Query("SELECT t FROM TipoVehiculo t WHERE t.nombre = :nombre")
    Optional<TipoVehiculo> findByNombre(String nombre);
}
