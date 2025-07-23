package com.neology.estacionamiento.application.service;

import com.neology.estacionamiento.domain.model.*;
import com.neology.estacionamiento.domain.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de vehículos (altas, consultas, modificaciones).
 * Implementa los casos de uso relacionados con el registro de vehículos.
 */
@Service
@Transactional
public class VehiculoService implements IVehiculoService {

    private final VehiculoRepository vehiculoRepository;

    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    /**
     * Registra un vehículo oficial en el sistema
     */
    public VehiculoOficial registrarVehiculoOficial(String placa) {
        validarPlaca(placa);
        
        if (vehiculoRepository.existsByPlaca(placa)) {
            throw new IllegalArgumentException("Ya existe un vehículo registrado con la placa: " + placa);
        }

        VehiculoOficial vehiculoOficial = new VehiculoOficial(placa);
        return (VehiculoOficial) vehiculoRepository.save(vehiculoOficial);
    }

    /**
     * Registra un vehículo de residente en el sistema
     */
    public VehiculoResidente registrarVehiculoResidente(String placa) {
        validarPlaca(placa);
        
        if (vehiculoRepository.existsByPlaca(placa)) {
            throw new IllegalArgumentException("Ya existe un vehículo registrado con la placa: " + placa);
        }

        VehiculoResidente vehiculoResidente = new VehiculoResidente(placa);
        return (VehiculoResidente) vehiculoRepository.save(vehiculoResidente);
    }

    /**
     * Busca un vehículo por su placa
     */
    @Transactional(readOnly = true)
    public Optional<Vehiculo> buscarPorPlaca(String placa) {
        validarPlaca(placa);
        return vehiculoRepository.findByPlaca(placa);
    }

    /**
     * Busca vehículos por tipo
     */
    @Transactional(readOnly = true)
    public List<Vehiculo> buscarPorTipo(TipoVehiculo tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de vehículo no puede ser null");
        }
        return vehiculoRepository.findByTipo(tipo);
    }

    /**
     * Obtiene todos los vehículos registrados
     */
    @Transactional(readOnly = true)
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return vehiculoRepository.findAll();
    }

    /**
     * Elimina un vehículo del sistema
     */
    public void eliminarVehiculo(String placa) {
        validarPlaca(placa);
        
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado: " + placa));

        // Verificar que no tenga estancia activa
        if (vehiculo.getEstanciaActiva() != null) {
            throw new IllegalStateException("No se puede eliminar un vehículo con estancia activa");
        }

        vehiculoRepository.deleteByPlaca(placa);
    }

    /**
     * Convierte un vehículo no residente a residente
     */
    public VehiculoResidente convertirAResidente(String placa) {
        validarPlaca(placa);
        
        Vehiculo vehiculoExistente = vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado: " + placa));

        if (vehiculoExistente.getTipo() == TipoVehiculo.RESIDENTE) {
            throw new IllegalArgumentException("El vehículo ya es residente");
        }

        if (vehiculoExistente.getTipo() == TipoVehiculo.OFICIAL) {
            throw new IllegalArgumentException("No se puede convertir un vehículo oficial a residente");
        }

        // Verificar que no tenga estancia activa
        if (vehiculoExistente.getEstanciaActiva() != null) {
            throw new IllegalStateException("No se puede convertir un vehículo con estancia activa");
        }

        // Eliminar el vehículo no residente y crear uno residente
        vehiculoRepository.deleteByPlaca(placa);
        VehiculoResidente nuevoResidente = new VehiculoResidente(placa);
        return (VehiculoResidente) vehiculoRepository.save(nuevoResidente);
    }

    private void validarPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo no puede estar vacía");
        }
        if (placa.length() > 10) {
            throw new IllegalArgumentException("La placa no puede tener más de 10 caracteres");
        }
    }
} 