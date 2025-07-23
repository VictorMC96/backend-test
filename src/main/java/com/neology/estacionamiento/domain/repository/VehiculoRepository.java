package com.neology.estacionamiento.domain.repository;

import com.neology.estacionamiento.domain.model.TipoVehiculo;
import com.neology.estacionamiento.domain.model.Vehiculo;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de dominio para la gestión de vehículos.
 * Define las operaciones de persistencia necesarias sin acoplarse a la implementación.
 */
public interface VehiculoRepository {

    /**
     * Busca un vehículo por su placa
     */
    Optional<Vehiculo> findByPlaca(String placa);

    /**
     * Busca vehículos por tipo
     */
    List<Vehiculo> findByTipo(TipoVehiculo tipo);

    /**
     * Obtiene todos los vehículos registrados
     */
    List<Vehiculo> findAll();

    /**
     * Guarda un vehículo (crear o actualizar)
     */
    Vehiculo save(Vehiculo vehiculo);

    /**
     * Elimina un vehículo por su placa
     */
    void deleteByPlaca(String placa);

    /**
     * Verifica si existe un vehículo con la placa especificada
     */
    boolean existsByPlaca(String placa);
} 