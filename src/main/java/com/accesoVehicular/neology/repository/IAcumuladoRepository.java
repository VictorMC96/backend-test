package com.accesoVehicular.neology.repository;

import com.accesoVehicular.neology.model.Acumulado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAcumuladoRepository extends JpaRepository<Acumulado, Long> {

    @Query("SELECT A FROM Acumulado A WHERE A.vehiculo.placa = :placa AND A.activo = true")
    Acumulado findByPlacaAndActivoTrue(String placa);

    @Query("SELECT A FROM Acumulado A WHERE A.activo = true")
    List<Acumulado> findAllActive();

}
