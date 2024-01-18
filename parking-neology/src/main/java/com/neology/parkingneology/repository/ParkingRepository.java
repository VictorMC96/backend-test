package com.neology.parkingneology.repository;

import com.neology.parkingneology.model.Parking;
import com.neology.parkingneology.model.Vehicle;
import com.neology.parkingneology.utils.Type;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingRepository extends CrudRepository<Parking, String> {
    @Query("Select p from Parking p where p.plate = ?1")
    public Optional<Parking> getParkingByPlate(String plate);


}
