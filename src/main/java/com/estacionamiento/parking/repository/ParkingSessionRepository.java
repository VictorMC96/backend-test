package com.estacionamiento.parking.repository;

import com.estacionamiento.parking.domain.ParkingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {

    Optional<ParkingSession> findFirstByPlateAndOpenTrueOrderByEntryTimeAsc(String plate);

    List<ParkingSession> findByPlate(String plate);

    void deleteByRegisteredType(com.estacionamiento.parking.domain.VehicleType type);

}