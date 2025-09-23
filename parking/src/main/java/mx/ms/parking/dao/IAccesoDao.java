package mx.ms.parking.dao;

import mx.ms.parking.entity.Acceso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;

public interface IAccesoDao extends JpaRepository<Acceso,Long> {
    public Acceso findByIdVehiculoAndFhEntrada(Long idVehiculo, Calendar fhEntrada);

}
