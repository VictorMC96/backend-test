package com.neology.estacionamiento.infrastructure.persistence;

import com.neology.estacionamiento.domain.model.Estancia;
import com.neology.estacionamiento.domain.model.EstadoEstancia;
import com.neology.estacionamiento.domain.repository.EstanciaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JPA del repositorio de Estancia.
 * Extiende JpaRepository para operaciones CRUD básicas y implementa métodos personalizados.
 */
@Repository
public interface JpaEstanciaRepository extends JpaRepository<Estancia, Long>, EstanciaRepository {

    /**
     * Busca estancias por placa de vehículo y estado
     */
    @Override
    Optional<Estancia> findByVehiculoPlacaAndEstado(String placa, EstadoEstancia estado);

    /**
     * Busca todas las estancias de un vehículo ordenadas por fecha de entrada descendente
     */
    @Override
    List<Estancia> findByVehiculoPlacaOrderByFechaEntradaDesc(String placa);

    /**
     * Busca estancias por estado
     */
    @Override
    List<Estancia> findByEstado(EstadoEstancia estado);

    /**
     * Busca estancias en un rango de fechas
     */
    @Override
    List<Estancia> findByFechaEntradaBetween(@Param("inicio") Calendar inicio, @Param("fin") Calendar fin);

    /**
     * Query personalizada para encontrar estancias activas con tiempo mayor al especificado
     */
    @Query("SELECT e FROM Estancia e WHERE e.estado = :estado AND e.fechaEntrada < :tiempoLimite")
    List<Estancia> findEstanciasActivasConTiempoMayorA(@Param("estado") EstadoEstancia estado, @Param("tiempoLimite") Calendar tiempoLimite);

    // Los métodos problemáticos se mantienen como default en la interfaz padre
    // para evitar errores de compilación pero lanzan UnsupportedOperationException
} 