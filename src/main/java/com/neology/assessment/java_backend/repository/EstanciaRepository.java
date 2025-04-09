package com.neology.assessment.java_backend.repository;

import com.neology.assessment.java_backend.entity.Estancia;
import com.neology.assessment.java_backend.entity.TipoVehiculo;
import com.neology.assessment.java_backend.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface EstanciaRepository extends JpaRepository<Estancia, Long> {
    Optional<Estancia> findByVehiculoAndFechaSalidaIsNull(Vehiculo vehiculo);
    List<Estancia> findByVehiculoTipoVehiculoIdAndMesActualTrue(Long tipoVehiculoId);
}
