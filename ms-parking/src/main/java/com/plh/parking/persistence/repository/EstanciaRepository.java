package com.plh.parking.persistence.repository;

import com.plh.parking.persistence.entities.EstanciaEntity;
import com.plh.parking.persistence.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstanciaRepository extends JpaRepository<EstanciaEntity, Long> {

    Optional<EstanciaEntity> findFirstByVehicleAndExitTimeIsNull(VehicleEntity vehicle);

    // List<EstanciaEntity> findAllByVehicleAndExitTimeIsNotNull(VehicleEntity vehicle


    @Transactional
    @Query("DELETE FROM EstanciaEntity stay WHERE stay.vehicle.idType= :typeVehicle")
    void deleteByIdType(Integer typeVehicle);


    @Query("SELECT stay FROM EstanciaEntity stay JOIN FETCH stay.vehicle")
    List<EstanciaEntity> findAllFetch();


}
