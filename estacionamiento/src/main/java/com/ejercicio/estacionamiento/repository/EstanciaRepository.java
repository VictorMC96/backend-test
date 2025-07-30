package com.ejercicio.estacionamiento.repository;

import com.ejercicio.estacionamiento.model.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstanciaRepository extends JpaRepository<Estancia, Long> {
    Optional<Estancia> findByVehiculoPlacaAndHoraSalidaIsNull(String placa);
}
