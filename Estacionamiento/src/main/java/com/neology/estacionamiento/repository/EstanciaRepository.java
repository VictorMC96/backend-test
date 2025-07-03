package com.neology.estacionamiento.repository;

import com.neology.estacionamiento.domain.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstanciaRepository extends JpaRepository<Estancia, Long> {
}