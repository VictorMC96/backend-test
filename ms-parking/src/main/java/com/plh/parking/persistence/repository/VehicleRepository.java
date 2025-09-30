package com.plh.parking.persistence.repository;


import com.plh.parking.persistence.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    Optional<VehicleEntity> findByPlate(String plate);


    List<VehicleEntity> findByIdType(Integer idType);


    @Transactional
    @Modifying
    @Query("UPDATE VehicleEntity v SET v.totalMinutes= 0 WHERE v.idType = :IdType")
    void updateMinuteByIdType(Integer IdType);


}
