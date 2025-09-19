package com.estacionamiento.parkingsystem.repository;

import com.estacionamiento.parkingsystem.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPlaca(String placa);
    List<Vehiculo> findByTipo(TipoVehiculo tipo);

    @Query("SELECT v FROM VehiculoOficial v")
    List<VehiculoOficial> findAllOficiales();

    @Query("SELECT v FROM VehiculoResidente v")
    List<VehiculoResidente> findAllResidentes();

    @Query("SELECT v FROM VehiculoNoResidente v")
    List<VehiculoNoResidente> findAllNoResidentes();

}
