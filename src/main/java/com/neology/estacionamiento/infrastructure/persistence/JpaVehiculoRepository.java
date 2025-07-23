package com.neology.estacionamiento.infrastructure.persistence;

import com.neology.estacionamiento.domain.model.TipoVehiculo;
import com.neology.estacionamiento.domain.model.Vehiculo;
import com.neology.estacionamiento.domain.repository.VehiculoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementación JPA del repositorio de vehículos.
 * Extiende JpaRepository para operaciones CRUD básicas y agrega consultas personalizadas.
 */
@Repository
public interface JpaVehiculoRepository extends JpaRepository<Vehiculo, String>, VehiculoRepository {

    /**
     * Busca vehículos por tipo usando la columna discriminator
     */
    @Query("SELECT v FROM Vehiculo v WHERE TYPE(v) = :tipo")
    List<Vehiculo> findByTipoClass(@Param("tipo") Class<? extends Vehiculo> tipo);

    @Override
    @Query("SELECT v FROM Vehiculo v WHERE v.placa = :placa")
    Optional<Vehiculo> findByPlaca(@Param("placa") String placa);

    @Override
    @Query("SELECT v FROM Vehiculo v WHERE v.tipo = :tipo")
    List<Vehiculo> findByTipo(@Param("tipo") TipoVehiculo tipo);

    @Override
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Vehiculo v WHERE v.placa = :placa")
    boolean existsByPlaca(@Param("placa") String placa);

    @Override
    default void deleteByPlaca(String placa) {
        findByPlaca(placa).ifPresent(this::delete);
    }
} 