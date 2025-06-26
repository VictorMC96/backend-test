package com.daniel_lopez_rivera.backend_test.repository;

import com.daniel_lopez_rivera.backend_test.model.Vehiculo;
import com.daniel_lopez_rivera.backend_test.util.VehiculoTipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {

    List<Vehiculo> findByTipo(VehiculoTipo tipo);

    Optional<Vehiculo> findById(String placa);
}