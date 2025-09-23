package com.ar21.pruebatecnica.repositories;

import com.ar21.pruebatecnica.entities.EntradaSalida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntradaSalidaRepository extends JpaRepository<EntradaSalida, Long> {

    Optional<EntradaSalida> findByPlaca(String placa);
}
