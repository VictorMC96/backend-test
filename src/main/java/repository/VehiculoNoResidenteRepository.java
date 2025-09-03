package repository;

import models.VehiculoNoResidente;
import models.VehiculoResidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoNoResidenteRepository extends JpaRepository<VehiculoNoResidente, Long> {
}