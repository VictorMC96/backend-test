package com.neology.parking.repository;

import com.neology.parking.model.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {
	Optional<Stay> findByVehicle_PlateAndExitTimeIsNull(String plate);
}
