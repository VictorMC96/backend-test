package com.estacionamiento.parkingsystem.repository;

import com.estacionamiento.parkingsystem.model.Estancia;
import com.estacionamiento.parkingsystem.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstanciaRepository extends JpaRepository <Estancia, Long>{
    List<Estancia> findByVehiculo(Vehiculo vehiculo);
    Estancia findFirstByVehiculoAndHoraSalidaIsNullOrderByHoraEntradaDesc(Vehiculo vehiculo);
    List<Estancia> findAllByVehiculo(Vehiculo vehiculo);
}
