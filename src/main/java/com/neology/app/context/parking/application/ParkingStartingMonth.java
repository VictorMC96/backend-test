package com.neology.app.context.parking.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neology.app.context.account.domain.ResidentAccountRepository;
import com.neology.app.context.parking.domain.ParkingRegisterRepository;
import com.neology.app.context.vehicle.domain.VehicleType;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParkingStartingMonth {

    private final ParkingRegisterRepository parkingRegisterRepository;
    private final ResidentAccountRepository residentAccountRepository;

    @Transactional
    public void run() {
        this.parkingRegisterRepository.deleteByRegisteredType(VehicleType.OFFICIAL);
        this.residentAccountRepository.findAll().forEach((account) -> {
            account.setAccumulatedMinutes(0);
            this.residentAccountRepository.save(account);
        });
    }

}
