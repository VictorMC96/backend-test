package com.example.abrahamtest.Parking_management.repository;

import com.example.abrahamtest.Parking_management.model.ParkingRecord;
import com.example.abrahamtest.Parking_management.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
    List<ParkingRecord> findByVehicleAndExitTimeIsNull(Vehicle vehicle);
    List<ParkingRecord> findByVehicle(Vehicle vehicle);
}
