package com.estacionamiento.parkingsystem.service;

import com.estacionamiento.parkingsystem.model.Vehiculo;

import java.util.List;

public interface VehiculoService {
    List<Vehiculo> obtenerTodos();
    List<Vehiculo> obtenerPorTipo(String tipo);
    Vehiculo obtenerPorId(Long id);
    Vehiculo crearVehiculo(Vehiculo vehiculo);
    Vehiculo actualizarVehiculo(Long id, Vehiculo vehiculo);
    void eliminarVehiculo(Long id);
}
