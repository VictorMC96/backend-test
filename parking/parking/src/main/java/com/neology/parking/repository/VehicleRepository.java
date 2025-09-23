package com.neology.parking.repository;

import com.neology.parking.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	Optional<Vehicle> findByPlate(String plate);
}
