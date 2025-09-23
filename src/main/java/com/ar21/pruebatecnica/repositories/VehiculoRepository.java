package com.ar21.pruebatecnica.repositories;

import com.ar21.pruebatecnica.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo,Long> {

 Optional<Vehiculo> findByPlaca(String placa);


 boolean existsByPlaca(String placa);
}
