package com.parking.repository;

import com.parking.dto.Car;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ParkingSystemRepository extends CrudRepository<Car, String> {
    Car findByPlate(String plate);

    boolean existsByPlate(String plate);

    @Query("SELECT c.insideParkingLot FROM Car c WHERE c.plate = :plate")
    Boolean findInsideParkingLotByPlate(@Param("plate") String plate);
}
