package com.headhunter.parking.Parking.repository.dao;

import com.headhunter.parking.Parking.repository.entity.CatTypeVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatTypeVehiRepository extends JpaRepository<CatTypeVehicle,Integer> {

    Optional<CatTypeVehicle> getTypeVehicleByCode(String code);
}
