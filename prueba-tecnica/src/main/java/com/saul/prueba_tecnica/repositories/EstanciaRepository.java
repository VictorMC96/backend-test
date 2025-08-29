package com.saul.prueba_tecnica.repositories;

import com.saul.prueba_tecnica.entities.Estancia;
import com.saul.prueba_tecnica.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstanciaRepository extends JpaRepository<Estancia, Long> {

    List<Estancia> findByVehiculo(Vehiculo vehiculo);

    Estancia findTopByVehiculoOrderByEntradaDesc(Vehiculo vehiculo);
}
