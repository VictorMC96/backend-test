package com.neology.estacionamiento.repository;

import com.neology.estacionamiento.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {
    Optional<Vehiculo> findByPlacaAndHoraSalidaIsNull(String placa);
}
