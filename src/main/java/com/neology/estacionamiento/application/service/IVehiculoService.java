package com.neology.estacionamiento.application.service;

import com.neology.estacionamiento.domain.model.TipoVehiculo;
import com.neology.estacionamiento.domain.model.Vehiculo;
import com.neology.estacionamiento.domain.model.VehiculoOficial;
import com.neology.estacionamiento.domain.model.VehiculoResidente;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de vehículos
 * Define operaciones de alto nivel para el manejo de vehículos
 */
public interface IVehiculoService {
    
    /**
     * Registra un vehículo oficial
     * @param placa Placa del vehículo
     * @return Vehículo oficial registrado
     * @throws IllegalArgumentException Si la placa es inválida o ya existe
     */
    VehiculoOficial registrarVehiculoOficial(String placa);
    
    /**
     * Registra un vehículo de residente
     * @param placa Placa del vehículo
     * @return Vehículo residente registrado
     * @throws IllegalArgumentException Si la placa es inválida o ya existe
     */
    VehiculoResidente registrarVehiculoResidente(String placa);
    
    /**
     * Busca un vehículo por su placa
     * @param placa Placa del vehículo
     * @return Vehículo si existe
     * @throws IllegalArgumentException Si la placa es inválida
     */
    Optional<Vehiculo> buscarPorPlaca(String placa);
    
    /**
     * Lista vehículos por tipo
     * @param tipo Tipo de vehículo
     * @return Lista de vehículos del tipo especificado
     * @throws IllegalArgumentException Si el tipo es inválido
     */
    List<Vehiculo> buscarPorTipo(TipoVehiculo tipo);
    
    /**
     * Lista todos los vehículos registrados
     * @return Lista completa de vehículos
     */
    List<Vehiculo> obtenerTodosLosVehiculos();
    
    /**
     * Convierte un vehículo no residente a residente
     * @param placa Placa del vehículo
     * @return Vehículo residente convertido
     * @throws IllegalArgumentException Si la placa es inválida
     * @throws IllegalStateException Si no se puede convertir
     */
    VehiculoResidente convertirAResidente(String placa);
    
    /**
     * Elimina un vehículo del sistema
     * @param placa Placa del vehículo
     * @throws IllegalArgumentException Si la placa es inválida
     * @throws IllegalStateException Si tiene estancia activa
     */
    void eliminarVehiculo(String placa);
} 