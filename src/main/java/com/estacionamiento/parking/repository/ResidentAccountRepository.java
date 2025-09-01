package com.estacionamiento.parking.repository;

import com.estacionamiento.parking.domain.ResidentAccount;
import com.estacionamiento.parking.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResidentAccountRepository extends JpaRepository<ResidentAccount, Long> {

    Optional<ResidentAccount> findByVehicle(Vehicle vehicle);

}
