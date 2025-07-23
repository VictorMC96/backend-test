package com.neology.estacionamiento.application.service;

import com.neology.estacionamiento.domain.model.Estancia;
import java.math.BigDecimal;
import java.util.List;

/**
 * Interfaz del servicio de estacionamiento
 * Define operaciones de alto nivel para el manejo del estacionamiento
 */
public interface IEstacionamientoService {
    
    /**
     * Registra la entrada de un vehículo
     * @param placa Placa del vehículo
     * @return La estancia creada con la hora exacta de entrada
     * @throws IllegalArgumentException Si la placa es inválida
     * @throws IllegalStateException Si el vehículo ya tiene una estancia activa
     */
    Estancia registrarEntrada(String placa);
    
    /**
     * Registra la salida de un vehículo
     * @param placa Placa del vehículo
     * @return Importe calculado a pagar
     * @throws IllegalArgumentException Si la placa es inválida
     * @throws IllegalStateException Si no hay estancia activa
     */
    BigDecimal registrarSalida(String placa);
    
    /**
     * Obtiene todos los vehículos actualmente en el estacionamiento
     * @return Lista de estancias activas
     */
    List<Estancia> obtenerVehiculosActivos();
    
    /**
     * Inicia un nuevo mes realizando las operaciones de limpieza necesarias
     */
    void iniciarNuevoMes();
    
    /**
     * Consulta el historial de estancias de un vehículo
     * @param placa Placa del vehículo
     * @return Lista de estancias del vehículo
     */
    List<Estancia> consultarHistorialVehiculo(String placa);
} 