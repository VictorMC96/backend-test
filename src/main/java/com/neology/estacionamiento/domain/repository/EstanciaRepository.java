package com.neology.estacionamiento.domain.repository;

import com.neology.estacionamiento.domain.model.Estancia;
import com.neology.estacionamiento.domain.model.EstadoEstancia;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de datos relacionadas con Estancia.
 * Define el contrato para acceso a datos sin depender de la implementación específica.
 */
public interface EstanciaRepository {

    /**
     * Guarda una estancia en el repositorio
     */
    Estancia save(Estancia estancia);

    /**
     * Busca una estancia por su ID
     */
    Optional<Estancia> findById(Long id);

    /**
     * Busca estancias por placa de vehículo y estado
     */
    Optional<Estancia> findByVehiculoPlacaAndEstado(String placa, EstadoEstancia estado);

    /**
     * Busca todas las estancias de un vehículo ordenadas por fecha de entrada descendente
     */
    List<Estancia> findByVehiculoPlacaOrderByFechaEntradaDesc(String placa);

    /**
     * Busca estancias por estado
     */
    List<Estancia> findByEstado(EstadoEstancia estado);

    /**
     * Busca estancias en un rango de fechas
     */
    List<Estancia> findByFechaEntradaBetween(Calendar inicio, Calendar fin);

    /**
     * Elimina una estancia
     */
    void delete(Estancia estancia);

    /**
     * Métodos que lanzan UnsupportedOperationException - lógica movida al servicio
     */
    
    default List<Estancia> findByVehiculoTipoAndEstado(String tipoVehiculo, EstadoEstancia estado) {
        throw new UnsupportedOperationException("Usar servicio para filtrar por tipo de vehículo");
    }

    default void deleteByVehiculoTipoAndEstado(String tipoVehiculo, EstadoEstancia estado) {
        throw new UnsupportedOperationException("Usar servicio para eliminar por tipo de vehículo");
    }

    default List<Estancia> findEstanciasResidentes() {
        throw new UnsupportedOperationException("Usar servicio para obtener estancias de residentes");
    }
} 