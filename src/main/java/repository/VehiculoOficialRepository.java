package repository;

import jakarta.transaction.Transactional;
import models.VehiculoOficial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoOficialRepository extends JpaRepository<VehiculoOficial, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Estancia e WHERE e.vehiculo IN (SELECT v FROM VehiculoOficial v)")
    void eliminarEstanciasEnVehiculosOficiales();
}