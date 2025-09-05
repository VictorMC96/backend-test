package com.neology.app.context.parking.infrastructure.persitence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neology.app.context.parking.domain.ParkingRegister;
import com.neology.app.context.parking.domain.ParkingRegisterRepository;

public interface H2ParkingRegisterRepository extends JpaRepository<ParkingRegister, Long>, ParkingRegisterRepository {

}
