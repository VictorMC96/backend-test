package repository;

import jakarta.transaction.Transactional;
import models.VehiculoResidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoResidenteRepository extends JpaRepository<VehiculoResidente, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE VehiculoResidente r SET r.tiempoAcumulado = 0")
    void inicializarTiempoAcumulado();
}