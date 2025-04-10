package com.demo.pagos.demo.repository;

import com.demo.pagos.demo.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {}
