package com.accesoVehicular.neology.repository;

import com.accesoVehicular.neology.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IRegistroRepository extends JpaRepository<Registro, Long> {

    @Query("SELECT R FROM Registro R WHERE R.vehiculo.placa = :placa AND R.fechaHoraSalida IS NULL")
    Optional<Registro> findByPlacaAndFechaSalidaIsNull(String placa);
}
