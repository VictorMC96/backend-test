package com.neology.parkingneology.repository;

import com.neology.parkingneology.model.Vehicle;
import com.neology.parkingneology.utils.Type;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, String> {

    @Query("Select p from Vehicle p where p.plate = ?1")
    public Optional<Vehicle> getVehicleByPlate(String plate);

    @Query("Select p from Vehicle p where p.type = ?1")
    public List<Vehicle> getAllVehicleByType(Type type);
}
