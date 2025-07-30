package com.accesoVehicular.neology.repository;

import com.accesoVehicular.neology.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IVehiculoRepository extends JpaRepository<Vehiculo, Long> {

    @Query("SELECT V FROM Vehiculo V WHERE V.placa = :placa")
    Optional<Vehiculo> findByPlaca(String placa);

    @Query("SELECT V FROM Vehiculo V WHERE V.tipoVehiculo.nombre = :tipoVehiculoNombre")
    List<Vehiculo> findByTipoVehiculoNombre(String tipoVehiculoNombre);

}
