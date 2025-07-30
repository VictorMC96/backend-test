package com.headhunter.parking.Parking.repository.dao;

import com.headhunter.parking.Parking.repository.entity.VehicleEnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEnt, Short> {

    public Optional<VehicleEnt> findByNumberPlate(String numberPlate);

    @Query("SELECT v FROM VehicleEnt v WHERE v.idType.code = :typeCode")
    List<VehicleEnt> findByType(@Param("typeCode") String typeCode);

}
