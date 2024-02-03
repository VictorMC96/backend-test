package com.examen.oodoo.repository;

import com.examen.oodoo.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo,Long> {
    Vehiculo findByPlaca(String placa);
}