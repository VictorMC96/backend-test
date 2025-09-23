package mx.ms.parking.dao;

import mx.ms.parking.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVehiculoDao extends JpaRepository<Vehiculo,Long> {
public Vehiculo findByPlaca(String placas);
}
