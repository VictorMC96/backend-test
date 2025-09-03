package com.neology.estacionamiento.repository;

import com.neology.estacionamiento.model.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstanciaRepository extends JpaRepository<Estancia, Long> {
    Optional<Estancia> findByPlacaAndHoraSalidaIsNull(String placa);
}
