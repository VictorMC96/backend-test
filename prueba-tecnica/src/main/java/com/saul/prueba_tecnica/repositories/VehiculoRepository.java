package com.saul.prueba_tecnica.repositories;

import com.saul.prueba_tecnica.entities.Vehiculo;


import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {

}
