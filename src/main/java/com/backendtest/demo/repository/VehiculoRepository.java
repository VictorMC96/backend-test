package com.backendtest.demo.repository;

import com.backendtest.demo.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {

}

