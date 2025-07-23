package com.neology.estacionamiento.application.service;

import com.neology.estacionamiento.domain.model.*;
import com.neology.estacionamiento.domain.repository.EstanciaRepository;
import com.neology.estacionamiento.domain.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio principal para la gestión del estacionamiento.
 * Implementa los casos de uso principales: entrada, salida y consultas.
 */
@Service
@Transactional  
public class EstacionamientoService implements IEstacionamientoService {

    private final VehiculoRepository vehiculoRepository;
    private final EstanciaRepository estanciaRepository;

    @Autowired
    public EstacionamientoService(VehiculoRepository vehiculoRepository, EstanciaRepository estanciaRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.estanciaRepository = estanciaRepository;
    }

    /**
     * Registra la entrada de un vehículo al estacionamiento.
     * Si el vehículo no existe, se registra como no residente.
     * @return La estancia creada con la hora exacta de entrada
     */
    public Estancia registrarEntrada(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo no puede estar vacía");
        }

        // Buscar o crear vehículo
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
                .orElseGet(() -> {
                    // Si no existe, registrar como no residente
                    VehiculoNoResidente nuevoVehiculo = new VehiculoNoResidente(placa);
                    return vehiculoRepository.save(nuevoVehiculo);
                });

        // Verificar que no tenga una estancia activa
        Estancia estanciaActiva = vehiculo.getEstanciaActiva();
        if (estanciaActiva != null) {
            throw new IllegalStateException("El vehículo " + placa + " ya tiene una estancia activa");
        }

        // Crear y guardar nueva estancia
        Estancia nuevaEstancia = new Estancia(vehiculo);
        vehiculo.agregarEstancia(nuevaEstancia);
        estanciaRepository.save(nuevaEstancia);
        
        return nuevaEstancia; // ✅ Devolver la estancia con la hora real
    }

    /**
     * Registra la salida de un vehículo del estacionamiento.
     * Calcula el importe a pagar según el tipo de vehículo.
     * - Oficiales: importe = 0
     * - Residentes: importe = 0 (se acumula tiempo para facturación mensual)
     * - No residentes: importe = tarifa × minutos (pago inmediato)
     */
    public BigDecimal registrarSalida(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo no puede estar vacía");
        }

        // Buscar vehículo
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado: " + placa));

        // Buscar estancia activa
        Estancia estanciaActiva = estanciaRepository.findByVehiculoPlacaAndEstado(placa, EstadoEstancia.ACTIVA)
                .orElseThrow(() -> new IllegalStateException("No hay estancia activa para el vehículo: " + placa));

        // Registrar salida
        estanciaActiva.registrarSalida();
        estanciaRepository.save(estanciaActiva);

        // Manejar lógica específica por tipo de vehículo
        BigDecimal importeAPagar;
        
        if (vehiculo instanceof VehiculoResidente) {
            // Para residentes: acumular tiempo, NO cobrar ahora
            VehiculoResidente residente = (VehiculoResidente) vehiculo;
            residente.acumularTiempoMes(estanciaActiva.getMinutosEstancia());
            vehiculoRepository.save(residente);
            importeAPagar = BigDecimal.ZERO; // ✅ Residentes no pagan al salir
        } else {
            // Para oficiales y no residentes: usar el importe calculado en la estancia
            importeAPagar = estanciaActiva.getImportePagado();
        }

        return importeAPagar;
    }

    /**
     * Obtiene todos los vehículos actualmente en el estacionamiento
     */
    @Transactional(readOnly = true)
    public List<Estancia> obtenerVehiculosActivos() {
        return estanciaRepository.findByEstado(EstadoEstancia.ACTIVA);
    }

    /**
     * Inicia un nuevo mes: limpia datos de vehículos oficiales y residentes
     * - Elimina TODAS las estancias de vehículos oficiales (las activas se cancelan automáticamente)
     * - Reinicia el tiempo acumulado de vehículos residentes a cero
     */
    public void iniciarNuevoMes() {
        // PARTE 1: Eliminar TODAS las estancias de vehículos oficiales
        // Obtener estancias de todos los estados y filtrar por vehículos oficiales
        List<Estancia> estanciasActivas = estanciaRepository.findByEstado(EstadoEstancia.ACTIVA);
        List<Estancia> estanciasFinalizadas = estanciaRepository.findByEstado(EstadoEstancia.FINALIZADA);
        
        // Combinar todas las estancias y filtrar por vehículos oficiales
        List<Estancia> todasLasEstancias = new java.util.ArrayList<>();
        todasLasEstancias.addAll(estanciasActivas);
        todasLasEstancias.addAll(estanciasFinalizadas);
        
        List<Estancia> estanciasOficiales = todasLasEstancias.stream()
            .filter(e -> e.getVehiculo().getTipo() == TipoVehiculo.OFICIAL)
            .collect(java.util.stream.Collectors.toList());
        
        // Eliminar todas las estancias de vehículos oficiales
        // (Si había estancias activas, se considera que el vehículo "salió" automáticamente)
        for (Estancia estancia : estanciasOficiales) {
            estanciaRepository.delete(estancia);
        }

        // PARTE 2: Reiniciar tiempo acumulado de residentes
        List<Vehiculo> residentes = vehiculoRepository.findByTipo(TipoVehiculo.RESIDENTE);
        for (Vehiculo vehiculo : residentes) {
            if (vehiculo instanceof VehiculoResidente) {
                VehiculoResidente residente = (VehiculoResidente) vehiculo;
                residente.reiniciarTiempoMes(); // ✅ tiempoAcumuladoMes = 0
                vehiculoRepository.save(residente);
            }
        }
    }

    /**
     * Consulta el historial de estancias de un vehículo
     */
    public List<Estancia> consultarHistorialVehiculo(String placa) {
        return estanciaRepository.findByVehiculoPlacaOrderByFechaEntradaDesc(placa);
    }
} 